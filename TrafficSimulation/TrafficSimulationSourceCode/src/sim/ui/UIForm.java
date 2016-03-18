package sim.ui;

final public class UIForm {
	private final String _heading;
	private final Pair[] _form;

	UIForm(String heading, Pair[] menu) {
		this._heading = heading;
		this._form = menu;
	}
	public int size() {
		return this._form.length;
	}
	public String getHeading() {
		return this._heading;
	}
	public String getPrompt(int i) {
		return this._form[i]._prompt;
	}
	public boolean checkInput(int i, String input) {
		if (null == this._form[i])
			return true;
		return this._form[i]._test.run(input);
	}

	static final class Pair {
		final String _prompt;
		final UIFormTest _test;

		Pair(String thePrompt, UIFormTest theTest) {
			this._prompt = thePrompt;
			this._test = theTest;
		}
	}
}
