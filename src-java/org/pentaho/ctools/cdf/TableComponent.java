/*!*****************************************************************************
 *
 * Selenium Tests For CTools
 *
 * Copyright (C) 2002-2014 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.ctools.cdf;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.pentaho.ctools.suite.CToolsTestSuite;
import org.pentaho.ctools.utils.ElementHelper;

import static org.junit.Assert.*;

/**
 * Testing the functionalies related with Tables, paging, sort, display rows,
 * search in table contents.
 *
 * Naming convention for test:
 *  'tcN_StateUnderTest_ExpectedBehavior'
 *
 * Issues History:
 * - CDF-346: validate paging, because previous we only had 10 entries of data.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TableComponent {
  // Instance of the driver (browser emulator)
  private static WebDriver driver;
  // Instance to be used on wait commands
  private static Wait<WebDriver> wait;
  // The base url to be append the relative url in test
  private static String baseUrl;

  /**
   * Shall inicialized the test before run each test case.
   */
  @BeforeClass
  public static void setUp(){
    driver = CToolsTestSuite.getDriver();
    wait = CToolsTestSuite.getWait();
    baseUrl = CToolsTestSuite.getBaseUrl();

    //Go to sample
    init();
  }

  /**
   * Go to the TableComponent web page.
   */
  public static void init(){
    //The URL for the TableComponent under CDF samples
    //This samples is in: Public/plugin-samples/CDF/Documentation/Component Reference/Core Components/Table Component
    driver.get(baseUrl + "api/repos/:public:plugin-samples:pentaho-cdf:30-documentation:30-component_reference:10-core:64-TableComponent:table_component.xcdf/generatedContent");

    //Wait for visibility of 'TableComponent'
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='dashboardContent']/div/div/div/h2/span[2]")));
  }

  /**
   * ############################### Test Case 1 ###############################
   *
   * Test Case Name:
   *    Validate Page Contents
   * Description:
   *    Here we want to validate the page contents.
   * Steps:
   *    1. Check the widget's title.
   */
  @Test
  public void tc1_PageContent_DisplayTitle() {
    // Validate the sample that we are testing is the one
    assertEquals("Community Dashboard Framework", driver.getTitle());
    ElementHelper.IsElementDisplayed(driver, By.xpath("//div[@id='dashboardContent']/div/div/div/h2/span[2]"));
    assertEquals("TableComponent", driver.findElement(By.xpath("//div[@id='dashboardContent']/div/div/div/h2/span[2]")).getText());
  }

  /**
   * ############################### Test Case 2 ###############################
   *
   * Test Case Name:
   *    Reload Sample
   * Description:
   *    Reload the sample (not refresh page).
   * Steps:
   *    1. Click in Code and then click in button 'Try me'.
   */
  @Test
  public void tc2_ReloadSample_SampleReadyToUse(){
    //## Step 1
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sample")));
    assertTrue(driver.findElement(By.id("sample")).isDisplayed());
    //Click in 'Code'
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='example']/ul/li[2]/a")));
    driver.findElement(By.xpath("//div[@id='example']/ul/li[2]/a")).click();
    assertFalse(driver.findElement(By.id("sample")).isDisplayed());
    //Click in 'Try me'
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#code > button")));
    driver.findElement(By.cssSelector("#code > button")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sample")));
    assertTrue(driver.findElement(By.id("sample")).isDisplayed());
  }

  /**
   * ############################### Test Case 3 ###############################
   *
   * Test Case Name:
   *    Check Paging
   * Description:
   *    User has the possibility to navegate between pages, and new date shall
   *    be displayed.
   * Steps:
   *    1. Check the data in first page is correct.
   *    2. Go to the next page and check the data.
   *    3. Go to the end page and check the data.
   *    4. Go to the first page and check the data.
   */
  @Test
  public void tc3_Paging_NavigateBetweenPages() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sampleObjectTable_length")));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sampleObjectTable_filter")));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sampleObjectTable")));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sampleObjectTable_info")));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sampleObjectTable_paginate")));

    //## Step 1
    assertEquals("Showing 1 to 10 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Amica Models & Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("94,117", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("200,995", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Baane Mini Imports", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Cruz & Sons Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("94,016", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());

    //## Step 2
    //Where we press NEXT
    WebElement page2 = driver.findElement(By.xpath("//a[@id='sampleObjectTable_next']"));
    assertNotNull(page2);
    page2.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[2][@class='paginate_button current']")));
    assertEquals("Showing 11 to 20 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Danish Wholesale Imports", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("145,042", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("174,140", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Extreme Desk Decorations, Ltd", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Handji Gifts& Co", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("115,499", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());

    //## Step 3
    //Where we press 5
    WebElement page5 = driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[5]"));
    assertNotNull(page5);
    page5.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[5][@class='paginate_button current']")));
    assertEquals("Showing 41 to 50 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Suominen Souveniers", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("113,961", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("160,010", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Toys of Finland, Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Vitachrome Inc.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("88,041", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());

    //## Step 4
    driver.findElement(By.xpath("//a[@id='sampleObjectTable_previous']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[4][@class='paginate_button current']")));
    driver.findElement(By.xpath("//a[@id='sampleObjectTable_previous']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[3][@class='paginate_button current']")));
    driver.findElement(By.xpath("//a[@id='sampleObjectTable_previous']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[2][@class='paginate_button current']")));
    driver.findElement(By.xpath("//a[@id='sampleObjectTable_previous']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[@class='paginate_button current']")));

    assertEquals("Showing 1 to 10 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Amica Models & Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("94,117", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("200,995", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Baane Mini Imports", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Cruz & Sons Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("94,016", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());
  }

  /**
   * ############################### Test Case 4 ###############################
   *
   * Test Case Name:
   *    Sort
   * Description:
   *    Testing the sort on Customer and Sales, and when user is not in the
   *    first page.
   * Steps:
   *    1. Sort in Customer (Desc)
   *    2. Sort in Sales (Asc)
   *    3. Page to the third page
   *    4. Sort in Sales (Desc)
   *    5. Go to the next page and check the data.
   *    6. Go to the end page and check the data.
   *    7. Go to the first page and check the data.
   */
  @Test
  public void tc4_Sort_ElementsAreSort() {
    //## Step 1
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='sampleObjectTable']/thead/tr/th[@class='column0 string sorting_asc']")));
    driver.findElement(By.xpath("//table[@id='sampleObjectTable']/thead/tr/th")).click();//Set to DESC
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='sampleObjectTable']/thead/tr/th[@class='column0 string sorting_desc']")));
    //Check Data
    assertNotNull(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1][@class='paginate_button current']")));
    assertEquals("Showing 1 to 10 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Vitachrome Inc.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("88,041", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("118,008", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertTrue(driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText().contains("Toms"));
    assertEquals("Suominen Souveniers", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("113,961", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());

    //## Step 2
    driver.findElement(By.xpath("//table[@id='sampleObjectTable']/thead/tr/th[2]")).click();//Sort Sales to ASC
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='sampleObjectTable']/thead/tr/th[2][@class='column1 numeric sorting_asc']")));
    //Check Data
    assertNotNull(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1][@class='paginate_button current']")));
    assertEquals("Showing 1 to 10 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Collectable Mini Designs Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("87,489", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("88,805", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Amica Models & Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertTrue(driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText().contains("Toms"));
    assertEquals("100,307", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());

    //## Step 3
    WebElement page3 = driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[3]"));
    assertNotNull(page3);
    page3.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[3][@class='paginate_button current']")));
    //Check Data
    assertNotNull(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[3][@class='paginate_button current']")));
    assertEquals("Showing 21 to 30 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Handji Gifts& Co", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("115,499", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("117,714", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Corrida Auto Replicas, Ltd", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Scandinavian Gift Ideas", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("134,259", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());

    //## Step 4
    driver.findElement(By.xpath("//table[@id='sampleObjectTable']/thead/tr/th[2]")).click();//Sort Sales to DESC
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='sampleObjectTable']/thead/tr/th[2][@class='column1 numeric sorting_desc']")));
    //Check Data
    assertNotNull(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1][@class='paginate_button current']")));
    assertEquals("Showing 1 to 10 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Euro+ Shopping Channel", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("912,294", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("200,995", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Down Under Souveniers, Inc", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Kelly's Gift Shop", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("158,345", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());

    //## Step 5
    WebElement page2 = driver.findElement(By.xpath("//a[@id='sampleObjectTable_next']"));
    assertNotNull(page2);
    page2.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[2][@class='paginate_button current']")));
    assertNotNull(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[2][@class='paginate_button current']")));
    assertEquals("Showing 11 to 20 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("AV Stores, Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("157,808", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("151,571", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Danish Wholesale Imports", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Reims Collectables", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("135,043", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());

    //## Step 6
    driver.findElement(By.xpath("//a[@id='sampleObjectTable_next']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[3][@class='paginate_button current']")));
    driver.findElement(By.xpath("//a[@id='sampleObjectTable_next']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[4][@class='paginate_button current']")));
    driver.findElement(By.xpath("//a[@id='sampleObjectTable_next']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[5][@class='paginate_button current']")));
    assertNotNull(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[5][@class='paginate_button current']")));
    assertEquals("Showing 41 to 50 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertTrue(driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText().contains("Toms"));
    assertEquals("100,307", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("98,496", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Cruz & Sons Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Collectable Mini Designs Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("87,489", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());

    //## Step 7
    WebElement page1 = driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1]"));
    assertNotNull(page1);
    page1.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1][@class='paginate_button current']")));
    //Check Data
    assertNotNull(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1][@class='paginate_button current']")));
    assertEquals("Showing 1 to 10 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Euro+ Shopping Channel", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("912,294", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("200,995", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Down Under Souveniers, Inc", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Kelly's Gift Shop", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("158,345", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());


    //reset to inicial state
    driver.findElement(By.xpath("//table[@id='sampleObjectTable']/thead/tr/th")).click();//Set Customers to ASC
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='sampleObjectTable']/thead/tr/th[@class='column0 string sorting_asc']")));
    assertEquals("Showing 1 to 10 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Amica Models & Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("94,117", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("200,995", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Baane Mini Imports", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Cruz & Sons Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("94,016", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());
  }

  /**
   * ############################### Test Case 5 ###############################
   *
   * Test Case Name:
   *    Display Entries
   * Description:
   *    When select the number of entries, the table displayed the number of
   *    entries selected with data.
   * Steps:
   *    1. Select 25 and paging
   *    2. Select 50 (no paging)
   */
  @Test
  public void tc5_DisplayEntries_DisplayTheNumberOfEntriesSelected() {
    assertEquals("Showing 1 to 10 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Amica Models & Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("94,117", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("200,995", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Baane Mini Imports", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Cruz & Sons Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("94,016", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());

    //## Step 1
    Select displayEntries = new Select(driver.findElement(By.xpath("//div[@id='sampleObjectTable_length']/label/select")));
    displayEntries.selectByValue("25");
    assertEquals("Showing 1 to 25 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Amica Models & Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("94,117", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("200,995", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Baane Mini Imports", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Cruz & Sons Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("94,016", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());
    //11 to 25
    assertEquals("Dragon Souveniers, Ltd.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[14]/td")).getText());
    assertEquals("172,990", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[14]/td[2]")).getText());
    assertEquals("98,924", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[17]/td[2]")).getText());
    assertEquals("Handji Gifts& Co", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[20]/td")).getText());
    assertEquals("La Corne D'abondance, Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[25]/td")).getText());
    assertEquals("97,204", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[25]/td[2]")).getText());

    //## Step 2
    displayEntries = new Select(driver.findElement(By.xpath("//div[@id='sampleObjectTable_length']/label/select")));
    displayEntries.selectByValue("50");
    assertEquals("Showing 1 to 50 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Amica Models & Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("94,117", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("200,995", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Baane Mini Imports", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Cruz & Sons Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("94,016", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());
    //11 to 25
    assertEquals("Dragon Souveniers, Ltd.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[14]/td")).getText());
    assertEquals("172,990", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[14]/td[2]")).getText());
    assertEquals("98,924", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[17]/td[2]")).getText());
    assertEquals("Handji Gifts& Co", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[20]/td")).getText());
    assertEquals("La Corne D'abondance, Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[25]/td")).getText());
    assertEquals("97,204", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[25]/td[2]")).getText());
    //26 to 50
    assertEquals("Muscle Machine Inc", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[31]/td")).getText());
    assertEquals("197,737", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[31]/td[2]")).getText());
    assertEquals("151,571", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[39]/td[2]")).getText());
    assertEquals("Toys of Finland, Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[46]/td")).getText());
    assertEquals("Vitachrome Inc.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[50]/td")).getText());
    assertEquals("88,041", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[50]/td[2]")).getText());

    //Reset display to 10
    displayEntries = new Select(driver.findElement(By.xpath("//div[@id='sampleObjectTable_length']/label/select")));
    displayEntries.selectByValue("10");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[5]")));
    assertEquals("Showing 1 to 10 of 50 entries", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Amica Models & Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("94,117", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("200,995", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Baane Mini Imports", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Cruz & Sons Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("94,016", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());
  }

  /**
   * ############################### Test Case 6 ###############################
   *
   * Test Case Name:
   *    Search Engine
   * Description:
   *    When search for something the table is refresh with the contents
   *    searched.
   * Steps:
   *    1. Search for 'Co.' (Check paging, display entries, sort)
   *    2. Search for 'Euro' (Check paging, display entries, sort)
   *    3. Search for 'TODO' (no result)
   */
  @Test
  public void tc6_SearchEngine_TableDisplayedContentSearch() {
    //## Step 1
    driver.findElement(By.xpath("//div[@id='sampleObjectTable_filter']/label/input")).sendKeys("Co.");
    assertEquals("Showing 1 to 10 of 13 entries (filtered from 50 total entries)", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertTrue(driver.findElement(By.id("sampleObjectTable_previous")).isDisplayed());
    assertTrue(driver.findElement(By.id("sampleObjectTable_previous")).isEnabled());
    assertTrue(driver.findElement(By.id("sampleObjectTable_next")).isDisplayed());
    assertTrue(driver.findElement(By.id("sampleObjectTable_next")).isEnabled());
    //check paging 1 and 2
    assertTrue(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1]")).isEnabled());
    assertTrue(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1]")).isDisplayed());
    assertTrue(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[2]")).isEnabled());
    assertTrue(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[2]")).isDisplayed());
    assertEquals("Amica Models & Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("94,117", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());
    assertEquals("157,808", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());
    assertEquals("Cruz & Sons Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[6]/td")).getText());
    assertEquals("Saveley & Henriot, Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td")).getText());
    assertEquals("142,874", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[10]/td[2]")).getText());
    //Click Next
    driver.findElement(By.xpath("//a[@id='sampleObjectTable_next']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[2][@class='paginate_button current']")));
    assertEquals("Souveniers And Things Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[1]/td")).getText());
    assertEquals("Toys of Finland, Co.", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td")).getText());
    assertEquals("111,250", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr[3]/td[2]")).getText());

    //## Step 2
    driver.findElement(By.xpath("//div[@id='sampleObjectTable_filter']/label/input")).clear();
    driver.findElement(By.xpath("//div[@id='sampleObjectTable_filter']/label/input")).sendKeys("Euro");
    assertTrue(driver.findElement(By.id("sampleObjectTable_previous")).isDisplayed());
    assertTrue(driver.findElement(By.id("sampleObjectTable_previous")).isEnabled());
    assertTrue(driver.findElement(By.id("sampleObjectTable_next")).isDisplayed());
    assertTrue(driver.findElement(By.id("sampleObjectTable_next")).isEnabled());
    assertTrue(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1]")).isEnabled());
    assertTrue(driver.findElement(By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1]")).isDisplayed());
    assertFalse(ElementHelper.IsElementPresent(driver, 2, By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[2]")));
    assertEquals("Showing 1 to 1 of 1 entries (filtered from 50 total entries)", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("Euro+ Shopping Channel", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
    assertEquals("912,294", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td[2]")).getText());

    //## Step 3
    driver.findElement(By.xpath("//div[@id='sampleObjectTable_filter']/label/input")).clear();
    driver.findElement(By.xpath("//div[@id='sampleObjectTable_filter']/label/input")).sendKeys("TODO");
    assertTrue(driver.findElement(By.id("sampleObjectTable_previous")).isDisplayed());
    assertTrue(driver.findElement(By.id("sampleObjectTable_previous")).isEnabled());
    assertTrue(driver.findElement(By.id("sampleObjectTable_next")).isDisplayed());
    assertTrue(driver.findElement(By.id("sampleObjectTable_next")).isEnabled());
    assertFalse(ElementHelper.IsElementPresent(driver, 2, By.xpath("//div[@id='sampleObjectTable_paginate']/span/a[1]")));
    assertEquals("Showing 0 to 0 of 0 entries (filtered from 50 total entries)", driver.findElement(By.xpath("//div[@id='sampleObjectTable_info']")).getText());
    assertEquals("No matching records found", driver.findElement(By.xpath("//table[@id='sampleObjectTable']/tbody/tr/td")).getText());
  }

  @AfterClass
  public static void tearDown() { }
}
