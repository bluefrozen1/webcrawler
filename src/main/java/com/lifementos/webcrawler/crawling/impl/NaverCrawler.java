package com.lifementos.webcrawler.crawling.impl;

import com.lifementos.webcrawler.common.code.ErrorCode;
import com.lifementos.webcrawler.crawling.Crawable;
import com.lifementos.webcrawler.crawling.CrawlingException;
import com.lifementos.webcrawler.crawling.domain.RecruitmentNotice;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NaverCrawler implements Crawable {

    private static WebDriver driver;

    // 드라이버 설치 경로
    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH = "chromedriver.exe";

    public static String NAVER_CAREER_MAIN_TECH_URL = "https://recruit.navercorp.com/rcrt/list.do?srchClassCd=1000000";

    public static String NAVER_CAREER_LIST_XPATH = "//*[@id=\"naver\"]/div/section/div/div/div[2]/div[2]/ul/li";

    public static int MAX_SCROLL_SIZE = 1000;
    public static int MAX_RETRY_COUNT = 3;

    private static String COMPANY_NAME = "naver";


    @PostConstruct
    public void init() {
        // WebDriver 경로 설정
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // 2. WebDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);
    }

    public List<RecruitmentNotice> searchJobDescription() throws CrawlingException {

        List<RecruitmentNotice> recruitmentNotices = new ArrayList<>();
        int index = 1;

        try {
            // naver 채용공고 목록 페이지 이동
            driver.get(NAVER_CAREER_MAIN_TECH_URL);
            Thread.sleep(1000); // 페이지 로딩 대기 시간

            // 스크롤 내려서 최대 사이즈 확인
            List<WebElement> careerFullLinkElements = scrollByLinkSize(MAX_SCROLL_SIZE);
            int maxLinkSize = careerFullLinkElements == null ? 0 : careerFullLinkElements.size();

            while(true) {
                //waitForClickablility(driver, By.xpath("//*[@id=\"naver\"]/div/section/div/div/div[2]/div[2]/ul/li"), Duration.of(10, ChronoUnit.SECONDS));
                List<WebElement> careerLinkElements = driver.findElements(By.xpath(NAVER_CAREER_LIST_XPATH));

                if(index >= careerLinkElements.size()) {
                    // 처리한 사이즈보다 클때까지 스크롤 내리기
                    careerLinkElements = scrollByLinkSize(index + 1);

                    // 스크롤 내렸음에도 사이즈가 증가하지 않은 경우 종료
                    if(index >= careerLinkElements.size()) {
                        break;
                    }
                }

                Thread.sleep(500);

                WebElement careerDetailElement = careerLinkElements.get(index);

                String title = careerDetailElement.getText();

                moveAndClick(index, careerDetailElement);

                Thread.sleep(1000);

                String content = getJobDescriptionContent();

                log.info("Career Title={}, Content={}", title, content);

                recruitmentNotices.add(
                        RecruitmentNotice.builder()
                                .company(COMPANY_NAME)
                                .title(title)
                                .content(content)
                                .collectedAt(LocalDateTime.now())
                        .build());

                driver.navigate().back();
                Thread.sleep(1000);

                index++;

                if(index >= maxLinkSize) {
                    log.info("Finish Search. Total index={}", index);
                }
            }

            return recruitmentNotices;

        } catch (Exception e) {
            throw new CrawlingException(ErrorCode.CRAWLING_ERROR, e);
        } finally {
            // 브라우저 종료
            //driver.quit();
            driver.close();
        }
    }

    private String getJobDescriptionContent() {
        // 링크 내 글 얻기
        //WebElement element = driver.findElement(By.xpath("//*[@id=\"naver\"]/div/section/div/div/div[2]/div[1]/div[2]/div"));
        WebElement element = driver.findElement(By.xpath("//*[@id=\"naver\"]/div/section/div/div/div[2]/div[1]/div[2]"));

        return element.getText();
    }

    private boolean moveAndClick(int index, WebElement careerDetailElement) throws InterruptedException {

        boolean result = false;
        int tryCount = 0;

        while(!result && tryCount < MAX_RETRY_COUNT) {
            // 포인터 이동하지 않고 클릭 시 문제가 발생하여 아래 내용 추가함
            Actions actions = new Actions(driver);
            actions.moveToElement(careerDetailElement).perform();

            Thread.sleep(500);

            log.info("Click. index={}", index);
            try {
                waitForClickablility2(careerDetailElement, Duration.of(10, ChronoUnit.SECONDS));
                careerDetailElement.click(); // 잘 되다가 is not clickable at point 에러 남
                break;
            } catch(Exception e) {
                log.error("Click ERROR. index={}, e={}", index, e.toString());
                //webElement.sendKeys(Keys.ENTER);
                //JavascriptExecutor executor = (JavascriptExecutor)driver;
                //executor.executeScript("arguments[0].click();", webElement);
            } finally {
                tryCount++;
            }
        }

        return false;
    }

    public static WebElement waitForClickablility(By locator, Duration duration) {
        WebDriverWait wait = new WebDriverWait(driver, duration);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickablility2(WebElement webElement, Duration duration) {
        WebDriverWait wait = new WebDriverWait(driver, duration);
        return wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    private static List<WebElement> scrollByLinkSize(int searchMaxSize) {
        // 초기 최대 사이즈 획득
        int currentMaxSize = driver.findElements(By.xpath(NAVER_CAREER_LIST_XPATH)).size();

        while(true) {
            // 스크롤 내림
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            try { Thread.sleep(100); } catch (InterruptedException e) {}

            // 스크롤 내린 후 사이즈 획득
            List<WebElement> elements = driver.findElements(By.xpath(NAVER_CAREER_LIST_XPATH));
            int currnetSize = elements.size();

            // 스크롤 내렸음에도 사이즈가 커지지 않은 경우
            // 또는 최대 검색 사이즈만큼 스크롤링 된 경우
            if(currentMaxSize == currnetSize
                || currnetSize >= searchMaxSize) {
                return elements;
            }

            currentMaxSize = currnetSize;
        }
    }

}