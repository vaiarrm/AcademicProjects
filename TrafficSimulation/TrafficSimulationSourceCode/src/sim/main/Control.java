package sim.main;

import sim.participants.Model;
import sim.participants.SimulationProperties;
import sim.ui.UI;
import sim.ui.UIError;
import sim.ui.UIForm;
import sim.ui.UIFormBuilder;
import sim.ui.UIFormTest;
import sim.ui.UIMenu;
import sim.ui.UIMenuAction;
import sim.ui.UIMenuBuilder;
/**
 * @author vaibhavsharma
 *
 */
class Control {
	private static final int EXITED = 0;
	private static final int EXIT = 1;
	private static final int START = 2;
	private static final int SUBMENU = 3;
	private static final int NUMSTATES = 4;
	private static final int NUMSUBSTATES = 16;
	private UIMenu[] _menu;
	private UIMenu[] _submenu;
	private int _state;

	private UIFormTest _integerTest;
	private UIFormTest _doubleTest;
	private UIForm _newTimeStep;
	private UIForm _newRunTime;
	private UIForm _numRows;
	private UIForm _numColumns;
	private UIForm _alt;
	private UIForm _min;
	private UIForm _max;

	private Model _model;
	private UI _ui;
	private SimulationProperties _prop;

	Control (UI ui){
		this._prop = SimulationProperties.getProperties();
		this._ui = ui;
		this._menu = new UIMenu[NUMSTATES];
		this._submenu = new UIMenu[NUMSUBSTATES];
		this._state = START;
		addSTART(START);
		addEXIT(EXIT);
		addSUBMENU(SUBMENU);
		this._integerTest = new UIFormTest() {
			public boolean run(String input) {
				try {
					Integer.parseInt(input);
					return true;
				} catch (NumberFormatException e) {
					return false;
				}
			}
		};
		this._doubleTest = new UIFormTest() {
			public boolean run(String input) {
				try {
					Double.parseDouble(input);
					return true;
				} catch (NumberFormatException e) {
					return false;
				}
			}
		};
		
		UIFormBuilder newTimeStep = new UIFormBuilder();
		newTimeStep.add("Enter New Time Step", _doubleTest);
		_newTimeStep = newTimeStep.toUIForm("Enter New Time Step");
		
		UIFormBuilder newRunTime = new UIFormBuilder();
		newRunTime.add("Enter New Run Time", _doubleTest);
		_newRunTime = newRunTime.toUIForm("Enter New Run Time");
		
		UIFormBuilder numRows = new UIFormBuilder();
		numRows.add("Enter Number of Rows", _integerTest);
		_numRows = numRows.toUIForm("Enter number");
		
		UIFormBuilder numColumns = new UIFormBuilder();
		numColumns.add("Enter number of columns", _integerTest);
		_numColumns = numColumns.toUIForm("Enter number");
		
		UIFormBuilder alt = new UIFormBuilder();
		alt.add("Simple Model\n 2. Alternating Model", _integerTest);
		//alt.add("Alternating Model", _integerTest);
		_alt= alt.toUIForm("Choose Model");
		
		UIFormBuilder min = new UIFormBuilder();
		min.add("Minimum Value", _doubleTest);
		_min = min.toUIForm("Enter Minimum Number");
		
		UIFormBuilder max = new UIFormBuilder();
		max.add("Maximum Value", _doubleTest);
		_max = max.toUIForm("Enter Maximum Number");		
	}
	private void addSTART(int stateNum) {
		UIMenuBuilder m = new UIMenuBuilder();
		m.add("Default", 
				new UIMenuAction() {
					public void run() {
						_ui.displayError("doh!");
					}
				});
		m.add("Run simulation", 
				new UIMenuAction() {
			public void run() {	
				_model = _prop.getModel();
				_model.run();
				_model.dispose();
				System.exit(0);
			}
		});

		m.add("Change simulation parameters", 
				new UIMenuAction() {
			public void run() {
				_state = SUBMENU;
			}
		});

		m.add("Exit",
				new UIMenuAction() {
			public void run() {
				_state = EXIT;
			}
		});

		_menu[stateNum] = m.toUIMenu("Traffic Simulation");
	}
	private void addSUBMENU(int stateNum){
		UIMenuBuilder m = new UIMenuBuilder();
		m.add("Default", 
				new UIMenuAction() {
			public void run() {
				_ui.displayError("doh!");
			}
		});
		m.add("Show Current Values", new UIMenuAction() {
			public void run() {
				_ui.displayMessage(_prop.toString());
			}
		});
		m.add("Simulation Time Step", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_newTimeStep);
				_prop.setSimulationTimeStep(Double.parseDouble(result[0]));
			}
		});
		m.add("Simulation Run Time", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_newRunTime);
				_prop.setSimulaitonRunTime(Double.parseDouble(result[0]));
			}
		});
		m.add("Grid Size", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_numRows);
				_prop.setNumRoadsRows(Integer.parseInt(result[0]));
				result = _ui.processForm(_numColumns);
				_prop.setNumRoadsColumns(Integer.parseInt(result[0]));
			}
		});
		m.add("Traffic Pattern", new UIMenuAction() {
			public void run() {
				String[] result = _ui.processForm(_alt);
				int num = Integer.parseInt(result[0]);
				if (num == 1){
					_prop.setAlternating(false);
				}else{
					_prop.setAlternating(true);
				}
			}
		});
		m.add("Car Entry Rate", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_min);
				_prop.setCarGenerationDelayMin(Double.parseDouble(result[0]));
				result = _ui.processForm(_max);
				_prop.setCarGenerationDelayMax(Double.parseDouble(result[0]));
			}
		});
		m.add("Road Segment Length", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_min);
				_prop.setRoadLengthMin(Double.parseDouble(result[0]));
				result = _ui.processForm(_max);
				_prop.setRoadLengthMax(Double.parseDouble(result[0]));
			}
		});
		m.add("Intersection Length", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_min);
				_prop.setIntersectionLengthMin(Double.parseDouble(result[0]));
				result = _ui.processForm(_max);
				_prop.setIntersectionLengthMax(Double.parseDouble(result[0]));
			}
		});
		m.add("Car Length", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_min);
				_prop.setCarLengthMin(Double.parseDouble(result[0]));
				result = _ui.processForm(_max);
				_prop.setCarLengthMax(Double.parseDouble(result[0]));
			}
		});
		m.add("Car Maximum Velocity", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_min);
				_prop.setCarVelocityMin(Double.parseDouble(result[0]));
				result = _ui.processForm(_max);
				_prop.setCarVelocityMax(Double.parseDouble(result[0]));
			}
		});
		m.add("Car Stop Distance", new UIMenuAction() {
			public void run() {
				String[] result = _ui.processForm(_min);
				_prop.setCarStopDistanceMin(Double.parseDouble(result[0]));
				result = _ui.processForm(_max);
				_prop.setCarStopDistanceMax(Double.parseDouble(result[0]));
			}
		});
		m.add("Car Brake Distance", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_min);
				_prop.setCarBrakeDistanceMin(Double.parseDouble(result[0]));
				result = _ui.processForm(_max);
				_prop.setCarBrakeDistanceMax(Double.parseDouble(result[0]));
			}
		});
		m.add("Traffic Light Green Time", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_min);
				_prop.setTrafficLightGreenMin(Double.parseDouble(result[0]));
				result = _ui.processForm(_max);
				_prop.setTrafficLightGreenMax(Double.parseDouble(result[0]));
			}
		});
		m.add("Traffic Light Yellow Time", new UIMenuAction() {
			public void run() {	
				String[] result = _ui.processForm(_min);
				_prop.setTrafficLightYellowMin(Double.parseDouble(result[0]));
				result = _ui.processForm(_max);
				_prop.setTrafficLightYellowMax(Double.parseDouble(result[0]));
			}
		});
		m.add("Reset Simulation And Return To Main", new UIMenuAction() {
			public void run() {
				_prop.setDefault();
				_prop = SimulationProperties.getProperties();
				_state = START;
			}
		});
		m.add("Go Back To Main Menu", new UIMenuAction() {
			public void run() {
				_state = START;
				
			}
		});
		_submenu[stateNum] = m.toUIMenu("Simulation Properties");
	}
	private void addEXIT(int stateNum) {
		UIMenuBuilder m = new UIMenuBuilder();

		m.add("Default", new UIMenuAction() { public void run() {} });
		m.add("Yes",
				new UIMenuAction() {
			public void run() {
				_state = EXITED;
			}
		});
		m.add("No",
				new UIMenuAction() {
			public void run() {
				_state = START;
			}
		});

		_menu[stateNum] = m.toUIMenu("Are you sure you want to exit?");
	}
	void run() {
		try {
			while (_state != EXITED) {
				if (_state == SUBMENU) {
					_ui.processMenu(_submenu[_state]);
				} else {
					_ui.processMenu(_menu[_state]);
				}
			}
		} catch (UIError e) {
			_ui.displayError("UI closed");
		}
	}
}
