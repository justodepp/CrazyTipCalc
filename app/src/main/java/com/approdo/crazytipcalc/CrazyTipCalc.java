package com.approdo.crazytipcalc;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class CrazyTipCalc extends Activity {
	
	private static final String TOTAL_BILL = "TOTAL_BILL";
	private static final String CURRENT_TIP = "CURRENT_TIP";
	private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";

	private double billBeforeTip;
	private double tipAmount;
	private double finalBill;
	
	EditText billBeforeTipET;
	EditText tipAmountET;
	EditText finalBillET;
	
	SeekBar tipSeekbar;
	
	// NEW PART ---------------
		     
		    // Sum of all radio buttons and check boxes
		     
	private int[] checklistValues = new int[12];
	  
	// Declare CheckBoxes
		     
	CheckBox friendlyCheckBox;
	CheckBox specialsCheckBox;
	CheckBox opinionCheckBox;
		     
	// Declare RadioButtons
		     
	RadioGroup availableRadioGroup;
	RadioButton availableBadRadio;
	RadioButton availableOKRadio;
	RadioButton availableGoodRadio;
	
	// Declare Spinner (Drop Down Box)
		     
	Spinner problemsSpinner;
		     
	// Declare Buttons
		     
	Button startChronometerButton;
	Button pauseChronometerButton;
	Button resetChronometerButton;
		     
	// Declare Chronometer
		     
	Chronometer timeWaitingChronometer;
		     
	// The number of seconds you spent
	// waiting for the waitress
		     
	long secondsYouWaited = 0;
		     
	// TextView for the chronometer
		     
	TextView timeWaitingTextView;
		     	     
	// END OF NEW PART ---------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crazy_tip_calc);
		
		if(savedInstanceState == null){
			billBeforeTip = 0.0;
			tipAmount = .15;
			finalBill = 0.0;	
		} else{
			billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
			tipAmount = savedInstanceState.getDouble(CURRENT_TIP);
			finalBill = savedInstanceState.getDouble(TOTAL_BILL);
		}
		
		billBeforeTipET = (EditText) findViewById(R.id.billEditText);
		tipAmountET = (EditText) findViewById(R.id.tipEditText);
		finalBillET = (EditText) findViewById(R.id.finalBillEditText);
		
		billBeforeTipET.addTextChangedListener(billBeforeTipListener);
		
		tipSeekbar = (SeekBar) findViewById(R.id.changeSeekBar);
		tipSeekbar.setOnSeekBarChangeListener(tipSeekbarListener);
		
		// NEW PART ---------------
			         
		// Initialize CheckBoxs
			         
		friendlyCheckBox = (CheckBox) findViewById(R.id.friendlyCheckBox);
		specialsCheckBox = (CheckBox) findViewById(R.id.specialsCheckBox);
        opinionCheckBox = (CheckBox) findViewById(R.id.opinionCheckBox);
         
        setUpIntroCheckBoxes(); // Add change listeners to check boxes
		         
        // Initialize RadioButtons
			         
        availableBadRadio = (RadioButton) findViewById(R.id.availableBadRadio);
        availableOKRadio = (RadioButton) findViewById(R.id.availableOkRadio);
        availableGoodRadio = (RadioButton) findViewById(R.id.availableGoodRadio);
		         
        // Initialize RadioGroups
		         
        availableRadioGroup = (RadioGroup) findViewById(R.id.availableRadioGroup);
			         
        // Add ChangeListener To Radio buttons
			         
        addChangeListenerToRadios();
			         
        // Initialize the Spinner
			         
        problemsSpinner = (Spinner) findViewById(R.id.problemsSpinner);	         
        problemsSpinner.setPrompt("Problem Solving");
		         
        // Add ItemSelectedListener To Spinner
			         
        addItemSelectedListenerToSpinner();
		         
        // Initialize Buttons
			         
        startChronometerButton = (Button) findViewById(R.id.startChronometerButton);
        pauseChronometerButton = (Button) findViewById(R.id.pauseChronometerButton);
        resetChronometerButton = (Button) findViewById(R.id.resetChronometerButton);
		         
        // Add setOnClickListeners for buttons
			         
        setButtonOnClickListeners();
			         
        // Initialize Chronometer
			         
        timeWaitingChronometer = (Chronometer) findViewById(R.id.timeWaitingChronometer);
			         
        // TextView for Chronometer
			         
        timeWaitingTextView = (TextView) findViewById(R.id.timeWaitingTextView);
		         
        // END OF NEW PART ---------------
	}
	
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putDouble(TOTAL_BILL, finalBill);
		outState.putDouble(CURRENT_TIP, tipAmount);
		outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
	}
	
	private OnSeekBarChangeListener tipSeekbarListener = new OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			tipAmount = (tipSeekbar.getProgress()) * .01;
			tipAmountET.setText(String.format("%.02f", tipAmount));
			updateTipAndFinalBill();
			
		}
	};
	
	private TextWatcher billBeforeTipListener = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			try{
				billBeforeTip = Double.parseDouble(s.toString());
				
			}catch(NumberFormatException e){
				billBeforeTip = 0.0;
			}
			updateTipAndFinalBill();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void updateTipAndFinalBill(){
		double tipAmount = Double.parseDouble(tipAmountET.getText().toString());
		double finalBill = billBeforeTip + (billBeforeTip * tipAmount);
		finalBillET.setText(String.format("%.02f", finalBill));
	}
	
	private void setUpIntroCheckBoxes(){
	     
	    // Add ChangeListener to the friendlyCheckBox
	     
	    friendlyCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

		    @Override
		    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		                 
		        // Use java ternary operator to set the right values for
		        // each item on the waitress check box checklist
		                 
		        checklistValues[0] = (friendlyCheckBox.isChecked())?4:0;
		         
		        // Calculate tip using the waitress checklist options		         
		        setTipFromWaitressChecklist();
		                 
		        // Update all the other EditTexts		                 
		        updateTipAndFinalBill();
		                 
		    	}
		});
	    
	    // Add ChangeListener to the specialsCheckBox
	     
	    specialsCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

	    	@Override
	        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
	                     
	            // Use java ternary operator to set the right values for
	            // each item on the waitress check box checklist
	                     
	            checklistValues[1] = (specialsCheckBox.isChecked())?1:0;
	             
	            // Calculate tip using the waitress checklist options	             
	            setTipFromWaitressChecklist();
	                     
	            // Update all the other EditTexts	                     
	            updateTipAndFinalBill();
	                     
	        }
	                 
	    });    
	     
	    // Add ChangeListener to the opinionCheckBox
	     
	    opinionCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

	        @Override
	        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
	                     
	            // Use java ternary operator to set the right values for
	            // each item on the waitress check box checklist
	                     
	            checklistValues[2] = (opinionCheckBox.isChecked())?2:0;
	             
	            // Calculate tip using the waitress checklist options	             
	            setTipFromWaitressChecklist();
	                     
	            // Update all the other EditTexts	                     
	            updateTipAndFinalBill();
	                     
	        }
	                 
	    });            
	     
	}
	
	// Calculate tip using the waitress checklist options
    
    private void setTipFromWaitressChecklist(){
         
        int checklistTotal = 0;
         
        // Cycle through all the checklist values to calculate
        // a total amount based on waitress performance
         
        for(int item : checklistValues){            
            checklistTotal += item;            
        }         
        // Set tipAmountET         
        tipAmountET.setText(String.format("%.02f", checklistTotal * .01));
    }
    
    private void addChangeListenerToRadios(){
        
        // Setting the listeners on the RadioGroups and handling them
        // in the same location
         
        availableRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            // checkedId is the RadioButton selected
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 
                // Use java ternary operator to set the right values for
                // each item on the waitress radiobutton checklist
                 
                checklistValues[3] = (availableBadRadio.isChecked())?-1:0;
                checklistValues[4] = (availableOKRadio.isChecked())?2:0;
                checklistValues[5] = (availableGoodRadio.isChecked())?4:0;
                 
                // Calculate tip using the waitress checklist options                
                setTipFromWaitressChecklist();
                         
                // Update all the other EditTexts                         
                updateTipAndFinalBill();
                 
            }
        });       
    }
    
    private void addItemSelectedListenerToSpinner(){
        
        problemsSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                 
                checklistValues[6] = (String.valueOf(problemsSpinner.getSelectedItem()).equals("Bad"))?-1:0;
                checklistValues[7] = (String.valueOf(problemsSpinner.getSelectedItem()).equals("OK"))?3:0;
                checklistValues[8] = (String.valueOf(problemsSpinner.getSelectedItem()).equals("Good"))?6:0;
                 
                // Calculate tip using the waitress checklist options
                setTipFromWaitressChecklist();
                         
                // Update all the other EditTexts                       
                updateTipAndFinalBill();
                 
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                 
            }
             
        });         
    }
    
    private void setButtonOnClickListeners(){
        
        startChronometerButton.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                 
                // Holds the number of milliseconds paused                 
                int stoppedMilliseconds = 0;
                 
                // Get time from the chronometer                 
                String chronoText = timeWaitingChronometer.getText().toString();
                String array[] = chronoText.split(":");
                if (array.length == 2) {
                     
                    // Find the seconds                    
                  stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                        + Integer.parseInt(array[1]) * 1000;
                } else if (array.length == 3) {
                     
                    // Find the minutes                     
                  stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                        + Integer.parseInt(array[1]) * 60 * 1000
                        + Integer.parseInt(array[2]) * 1000;
                }
                 
                // Amount of time elapsed since the start button was
                // pressed, minus the time paused
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                 
                // Set the number of seconds you have waited
                // This would be set for minutes in the real world
                // obviously. That can be found in array[2]                
                secondsYouWaited = Long.parseLong(array[1]);
                 
                updateTipBasedOnTimeWaited(secondsYouWaited);
                 
                // Start the chronometer
                 
                timeWaitingChronometer.start();
                 
            }
             
             
        });
         
        pauseChronometerButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {                
                timeWaitingChronometer.stop();                 
            }                      
        });
         
        resetChronometerButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {                 
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());                
                // Reset milliseconds waited back to 0                
                secondsYouWaited = 0;                
            }                        
        });         
    }

    private void updateTipBasedOnTimeWaited(long secondsYouWaited){
        
        // If you spent less then 10 seconds then add 2 to the tip
        // if you spent more then 10 seconds subtract 2         
        checklistValues[9] = (secondsYouWaited > 10)?-2:2;
         
        // Calculate tip using the waitress checklist options        
        setTipFromWaitressChecklist();
                 
        // Update all the other EditTexts                
        updateTipAndFinalBill();      
    }
}