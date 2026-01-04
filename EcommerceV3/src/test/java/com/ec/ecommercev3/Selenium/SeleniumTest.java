//package com.ec.ecommercev3.Selenium;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//class SeleniumTest {
//
//    @Test
//    void abrirGoogleEBuscar() throws InterruptedException {
//        WebDriverManager.chromedriver().setup();
//        WebDriver driver = new ChromeDriver();
//
//        try {
//            driver.get("https://www.google.com");
//            WebElement searchBox = driver.findElement(By.name("q"));
//            searchBox.sendKeys("Spring Boot Selenium");
//            searchBox.submit();
//
//            Thread.sleep(3000);
//            System.out.println("Título da página: " + driver.getTitle());
//        } finally {
//            driver.quit();
//        }
//    }
//
//    @Test
//    void testeLogin() throws InterruptedException {
//        WebDriverManager.chromedriver().setup();
//        WebDriver driver = new ChromeDriver();
//
//        try {
//            driver.get("http://localhost:5173/");
//            // Exemplo de interação; ajuste IDs conforme seu frontend
//            WebElement loginLink = driver.findElement(By.id("login"));
//            loginLink.click();
//            Thread.sleep(1000);
//        } finally {
//            driver.quit();
//        }
//    }
//}
