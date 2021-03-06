package com.zaorish.android.todo.test;

import java.util.List;

import junit.framework.Assert;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.RadioButton;

import com.jayway.android.robotium.solo.Solo;
import com.zaorish.android.todo.ToDoDetailsActivity;
import com.zaorish.android.todo.ToDoListActivity;

public class ToDoListTest extends ActivityInstrumentationTestCase2<ToDoListActivity> {

	private static final String TASK_NAME = "my task name";
	private static final String TASK_DESCRIPTION = "my task description";

	private Solo solo;

	public ToDoListTest() {
		super("com.zaorish.android.todo", ToDoListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());

		solo.clickOnMenuItem("Add Task");

		solo.assertCurrentActivity("expected activity", ToDoDetailsActivity.class);

		// configure a task
		solo.clearEditText(0);
		solo.enterText(0, TASK_NAME);

		solo.clearEditText(1);
		solo.enterText(1, TASK_DESCRIPTION);

		solo.clickOnRadioButton(0);
	}

	@Override
	protected void tearDown() throws Exception {
		// delete the task
		solo.clickLongInList(1);
		solo.clickOnText("Delete Task");

		// assert the task is no longer displayed
		solo.assertCurrentActivity("expected activity", ToDoListActivity.class);
		Assert.assertFalse(solo.searchText(TASK_NAME));

		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	}

	/**
	 * Scenario: User should be able to create new tasks <br>
	 * Given a user chooses to create a new task <br>
	 * And the user configures name, description and priority for the task <br>
	 * When the user saves the task <br>
	 * Then the user should see the details of the newly created task
	 */
	public void testCreateTask() throws Exception {
		// given a configured task

		// when we save the task
		solo.clickOnButton("Save");

		// then we should be redirected to the original activity
		solo.assertCurrentActivity("expected activity", ToDoListActivity.class);
		// and the name of the task just created should be displayed
		Assert.assertTrue(solo.searchText(TASK_NAME));
	}

	/**
	 * Scenario: User should be able to view the existing tasks <br>
	 * Given the user previously created some tasks <br>
	 * Then the user should see the details of the existing task
	 */
	public void testDisplayTheExistingTask() throws Exception {
		// given an existing task
		solo.clickOnButton("Save");

		// when we view the task
		solo.clickInList(1);
		solo.assertCurrentActivity("expected activity", ToDoDetailsActivity.class);

		// then we should see the details of the task
		List<EditText> textFields = solo.getCurrentEditTexts();
		Assert.assertTrue(textFields.get(0).getText().toString().equals(TASK_NAME));
		Assert.assertTrue(textFields.get(1).getText().toString().equals(TASK_DESCRIPTION));
		//
		List<RadioButton> radioButtons = solo.getCurrentRadioButtons();
		Assert.assertTrue(radioButtons.get(0).isChecked());
		Assert.assertFalse(radioButtons.get(1).isChecked());
		//
		solo.clickOnButton("Save");
	}

	/**
	 * Scenario: User should be able to modify the existing tasks <br>
	 * Given the user previously created some tasks <br>
	 * And the user chooses to modify an existing task <br>
	 * When the user saves the task <br>
	 * Then the user should see the details of the newly modified task
	 */
	public void testModifyTheExistingTask() throws Exception {
		// given an existing task
		solo.clickOnButton("Save");

		// when we edit the task
		solo.clickInList(1);
		solo.assertCurrentActivity("expected activity", ToDoDetailsActivity.class);

		solo.clearEditText(0);
		solo.enterText(0, "another name");

		solo.clearEditText(1);
		solo.enterText(1, "another description");

		solo.clickOnRadioButton(1);

		solo.clickOnButton("Save");

		// then we should be redirected to the original activity
		solo.assertCurrentActivity("expected activity", ToDoListActivity.class);
		// and the name of the task just updated should be displayed
		Assert.assertTrue(solo.searchText("another name"));
	}

	/**
	 * Scenario: User should be able to delete the existing tasks <br>
	 * Given the user previously created some tasks <br>
	 * When the user deletes one of the existing tasks <br>
	 * Then the user should not see the details of the deleted task
	 */
	public void testDeleteTask() throws Exception {
		// setUp + tearDown verify this one
	}

}
