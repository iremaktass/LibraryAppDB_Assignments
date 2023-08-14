package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.BorrowedBooksPage;
import com.library.pages.DashBoardPage;
import com.library.utility.BrowserUtil;
import com.library.utility.DB_Util;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.hc.core5.util.Asserts;
import org.junit.Assert;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

public class BooksStepDef {

    // US04
    BookPage bookPage = new BookPage();
    String globalBookName;

    @When("the user searches for {string} book")
    public void the_user_searches_for_book(String bookName) {
         bookPage = new BookPage();
        globalBookName = bookName;
        bookPage.bookSearch(bookName);
    }

    @When("the user clicks edit book button")
    public void the_user_clicks_edit_book_button() {
        bookPage.editBook(globalBookName).click();
    }

    @Then("book information must match the Database")
    public void book_information_must_match_the_database() {

        String queryForSpecificBook = "SELECT * from books where name='Book Borrow 2';";
        DB_Util.runQuery(queryForSpecificBook);

        Map<String, String> mapDataFromDB = DB_Util.getRowMap(1);
        System.out.println(mapDataFromDB);


        String name_db = mapDataFromDB.get("name");
        String name_ui = bookPage.getBookInfo("Book Name");
        Assert.assertEquals(name_db, name_ui);

        String isbnDB = mapDataFromDB.get("isbn");
        String isbnUI = bookPage.getBookInfo("ISBN");
        Assert.assertEquals(isbnUI, isbnDB);

        String yearDB = mapDataFromDB.get("year");
        String yearUI = bookPage.getBookInfo("Year");
        Assert.assertEquals(yearUI, yearDB);

        String authorDB = mapDataFromDB.get("author");
        String authorUI = bookPage.getBookInfo("Author");
        Assert.assertEquals(authorUI, authorDB);

        String bookCategoryDB = mapDataFromDB.get("book_category_id");
        String bookCategoryUI = bookPage.categoryDropdown.getAttribute("value");
        Assert.assertEquals(bookCategoryUI, bookCategoryDB);

        //SECOND WAY
        /*
        String query2 = "select b.name, b_c.name from books b inner join book_categories b_c ON b.book_category_id =b_c.id where b.name = 'Book Borrow 2';";

        DB_Util.runQuery(query2);
        Map<String, String> rowMap2 = DB_Util.getRowMap(1);

        String bookCategoryDB2 = rowMap2.get("book_categories.name");
        String bookCategoryUI2= bookPage.getBookInfo("Book Category");
        Assert.assertEquals(bookCategoryUI2,bookCategoryDB2);

         */
    }

    //BookPage bookPage2 = new BookPage();

        // US06
        @When("the librarian click to add book")
        public void the_librarian_click_to_add_book() {
            bookPage.addBook.click();
        }
    @When("the librarian enter book name {string}")
    public void the_librarian_enter_book_name(String name) {
        bookPage.bookName.sendKeys(name);
    }
    @When("the librarian enter ISBN {string}")
    public void the_librarian_enter_isbn(String isbn) {
        bookPage.isbn.sendKeys(isbn);

    }
    @When("the librarian enter year {string}")
    public void the_librarian_enter_year(String year) {
        bookPage.year.sendKeys(year);
    }
    @When("the librarian enter author {string}")
    public void the_librarian_enter_author(String author) {
        bookPage.author.sendKeys(author);
    }
    @When("the librarian choose the book category {string}")
    public void the_librarian_choose_the_book_category(String category) {
        BrowserUtil.selectOptionDropdown(bookPage.categoryDropdown,category);
    }
    @When("the librarian click to save changes")
    public void the_librarian_click_to_save_changes() {
        bookPage.saveChanges.click();
    }


    @Then("verify {string} message is displayed")
    public void verify_the_book_has_been_created_message_is_displayed(String expectedMessage) {
        // You can verify message itself too both works
        //OPT 1
        String actualMessage = bookPage.toastMessage.getText();
        Assert.assertEquals(expectedMessage,actualMessage);

        //OPT 2
        Assert.assertTrue(bookPage.toastMessage.isDisplayed());

    }

    @Then("verify {string} information must match with DB")
    public void verify_information_must_match_with_db(String expectedBookName) {
        String query = "select name, author, isbn from books\n" +
                "where name = '"+expectedBookName+"'";

        DB_Util.runQuery(query);

        Map<String, String> rowMap = DB_Util.getRowMap(1);

        String actualBookName = rowMap.get("name");

        Assert.assertEquals(expectedBookName,actualBookName);

    }


    // US07

    @When("the user clicks Borrow Book")
    public void the_user_clicks_borrow_book() {

        bookPage.borrowBook(globalBookName).click();
        BrowserUtil.waitFor(2);

    }

    @Then("verify that book is shown in {string} page")
    public void verify_that_book_is_shown_in_page(String module) {

        BorrowedBooksPage borrowedBooksPage = new BorrowedBooksPage();
        new DashBoardPage().navigateModule(module);

        Assert.assertTrue(BrowserUtil.getElementsText(borrowedBooksPage.allBorrowedBooksName).contains(globalBookName));
    }
    @Then("verify logged student has same book in database")
    public void verify_logged_student_has_same_book_in_database() {
        String query = "select name from books b\n" +
                "join book_borrow bb on b.id = bb.book_id\n" +
                "join users u on bb.user_id = u.id\n" +
                "where name = '"+globalBookName+"' and full_name = 'Test Student 5';";

        DB_Util.runQuery(query);
        List<String> actualList = DB_Util.getColumnDataAsList(1);
        Assert.assertTrue(actualList.contains(globalBookName));
    }


}
