package com.agdroid.recyclerviewdemo;

import com.agdroid.recyclerviewdemo.data.DataSourceInterface;
import com.agdroid.recyclerviewdemo.data.ListItem;
import com.agdroid.recyclerviewdemo.logic.Controller;
import com.agdroid.recyclerviewdemo.view.ViewInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * AG: Mock - Attrappe
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ControllerUnitTest {

    @Mock
    DataSourceInterface dataSource;

    @Mock
    ViewInterface view;


    /**
     * Often times we'll want data to run our tests against. I like to define such Data as a series
     * of Static variables in the Test file itself for convenience.
     */
    private static final ListItem testItem = new ListItem(
            "6:30AM 06/01/2017",
            "Look at Open Source Projects",
             R.color.RED);


    private static final int POSITION = 3;

    /**
     * Since we're testing our Controller Class, this one is the Real implementation!
     * How this works is we make a Real instance of the Controller talk to Fake Instances of
     * what it would normally talk to (i.e. our Mocks above). Since the Controller does most of the
     * thinking (Logic), it follows that it would benefit the most from a good suite of Tests.
     */
    Controller controller;

    /**
     * the @Before annotation makes this Code run before the Tests themselves. You can use this
     * to initialize the different components of your Test, should you need to. You can also
     * initialize things in the Test methods themselves. Whatever works best for your Use Case.
     */
    @Before
    public void setUpTest() {
        MockitoAnnotations.initMocks(this);
        controller = new Controller(view, dataSource);
    }

    @Test
    public void onGetListDataSuccessful()  {
        //Set up any data for the test
        ArrayList<ListItem> listOfData = new ArrayList<>();
        listOfData.add(testItem);

        //This is where we tell our "Mocks" what to do when our Controller talks to them. Since they
        //aren't real objects, we must tell them exactly what to do if we want responses from them.
        Mockito.when(dataSource.getListOfData()).thenReturn(listOfData);

        //Call the method(Unit) we are testing
        controller.getListFromDataSource();

        //Check how the Tested Class responds to the data it receives
        //or test it's behaviour
        Mockito.verify(view).setUpAdapterAndView(listOfData);
    }

    // @Test
    // public void onGetListDataUnsuccessful() {
    /**************************
     *
     * Unit Test Homework:
     *
     * Implement the "View", so that when we don't recieve a list, it shows some kind of
     * error message to the user. This is common practice that you should learn!
     *
     * I've written some hints you'll have to decipher into Java code:
     *************************/
    //1 Set up your Mock dataSource

    //2 Call the method you wish to test on the Controller

    //3 Verify that the View has been told to show a message (I'd suggest showing a Toast for now)

    //Profit???

    // }


    @Test
    public void onListItemClicked() {
        controller.onListItemClick(testItem);

        Mockito.verify(view).startDetailActivity(
                testItem.getDateAndTime(),
                testItem.getMessage(),
                testItem.getColorResource());
    }


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}