package com.lifementos.webcrawler.test;

import org.apache.http.io.SessionOutputBuffer;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

public class NaverLogin {

    private static WebDriver driver;

    // 드라이버 설치 경로
    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH = "chromedriver.exe";

    public static String NAVER_CAREER_MAIN_TECH_URL = "https://recruit.navercorp.com/rcrt/list.do?srchClassCd=1000000";
    public static String NAVER_CAREER_LIST_XPATH = "//*[@id=\"naver\"]/div/section/div/div/div[2]/div[2]/ul/li";

    public NaverLogin (){
        // WebDriver 경로 설정
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // 2. WebDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);
    }

    public void activateBot() {

        int index = 0;

        try {
            // naver 채용공고 목록 페이지 이동
            driver.get(NAVER_CAREER_MAIN_TECH_URL);
            Thread.sleep(1000); // 페이지 로딩 대기 시간

            // 스크롤 내려서 최대 사이즈 확인
            List<WebElement> careerFullLinkElements = scrollByLinkSize(1000);
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
                //webElement.sendKeys(Keys.ENTER);
                //webElement.sendKeys("\n");
                //executor.executeScript("arguments[0].click();", webElement);

                //waitForClickablility2(driver, webElement, Duration.of(10, ChronoUnit.SECONDS));



                // 포인터 이동하지 않고 클릭 시 문제가 발생하여 아래 내용 추가함
                Actions actions = new Actions(driver);
                actions.moveToElement(careerDetailElement).perform();

                Thread.sleep(500);

                System.out.println(index + " 클릭.");
                try {
                    careerDetailElement.click(); // 잘 되다가 is not clickable at point 에러 남
                } catch(Exception e) {
                    System.out.println(index + " 클릭(실패) " + e.toString());
                    careerDetailElement.sendKeys(Keys.ENTER);
                    continue;
                }

                //JavascriptExecutor executor = (JavascriptExecutor)driver;
                //executor.executeScript("arguments[0].click();", webElement);

                Thread.sleep(1000);
                // 링크 내 글 얻기
                WebElement element = driver.findElement(By.xpath("//*[@id=\"naver\"]/div/section/div/div/div[2]/div[1]/div[2]/div"));

                if(element == null) {
                    element = driver.findElement(By.xpath("//*[@id=\"naver\"]/div/section/div/div/div[2]/div[1]/div[2]"));
                }

                System.out.println("!!!!!!!!!!!! " + element.getText());
                driver.navigate().back();
                Thread.sleep(1000);
                index++;

                if(index >= maxLinkSize) {
                    System.out.println("Finish Search. index=" + index);
                }
            }



//            // 4. 로그인 버튼 클릭
//            element = driver.findElement(By.className("link_login"));
//            element.click();
//
//            Thread.sleep(1000);
//
//            // ID 입력
//            element = driver.findElement(By.id("id"));
//            element.sendKeys("bluefrozen");
//
//            // 비밀번호 입력
//            element = driver.findElement(By.id("pw"));
//            element.sendKeys("비밀번호입니다");
//
//            // 전송
//            element = driver.findElement(By.className("btn_global"));
//            element.submit();
        }catch (Exception e) {
            System.out.println("클릭 에러 " + e.toString());
            e.printStackTrace();
        } finally {
            System.out.println("END. size=" + index);
            // 5. 브라우저 종료
            //driver.quit();
            driver.close();
        }
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

    public static void main(String[] args) {
        NaverLogin bot1 = new NaverLogin();
        bot1.activateBot();
    }
}