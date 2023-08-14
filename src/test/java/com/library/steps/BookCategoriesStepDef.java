package com.library.steps;

import com.library.pages.BookPage;
import com.library.utility.BrowserUtil;
import com.library.utility.DB_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.List;

public class BookCategoriesStepDef {

    // US03
    BookPage bookPage;
    //  BookPage bookPage=new BookPage();
    /*
    if we initialize our pages at the class level and run DB we will have one blank browser why ?
    We are calling constructor of Base age and inside the constructor we are calling Driver.getDriver() method and
    this method is opening one empty browser for us.
     */

    @When("the user navigates to {string} page")
    public void the_user_navigates_to_page(String moduleName) {
        bookPage= new BookPage();
        bookPage.navigateModule(moduleName);

        BrowserUtil.waitFor(2);
    }

    List<String> actualBookCategories;
    @When("the user clicks book categories")
    public void the_user_clicks_book_categories() {

        actualBookCategories= BrowserUtil.getAllSelectOptions(bookPage.mainCategoryElement);

        actualBookCategories.remove(0);
    }
    @Then("verify book categories must match book_categories table from db")
    public void verify_book_categories_must_match_book_categories_table_from_db() {

        String query = "select name from book_categories";
        DB_Util.runQuery(query);

        List<String> expectedBookCategories = DB_Util.getColumnDataAsList(1);

        Assert.assertEquals(expectedBookCategories, actualBookCategories);

    }




    //US05

    @When("I execute query to find most popular book genre")
    public void i_execute_query_to_find_most_popular_book_genre() {

        String mostPopularBookGenre = "select name from book_categories\n" +
                "         where id = (select book_category_id from books\n" +
                "            where id = (select book_id from book_borrow group by book_id order by count(*) desc limit 1));";
        DB_Util.runQuery(mostPopularBookGenre);
    }
    @Then("verify {string} is the most popular book genre.")
    public void verify_is_the_most_popular_book_genre(String expectedGenre) {

        String actualGenre = DB_Util.getFirstRowFirstColumn();

        Assert.assertEquals(expectedGenre,actualGenre);

    }
}
