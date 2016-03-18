package sim.agent;

import java.util.Observable;


final class TimeServerLinked extends Observable implements TimeServer {

	private static final class Node {
		final double waketime;
		final Agent agent;
		Node next;

		public Node(double waketime, Agent agent, Node next) {
			this.waketime = waketime;
			this.agent = agent;
			this.next = next;
		}
	}
	private double _currentTime;
	private int _size;
	private Node _head;

	TimeServerLinked() {
		this._size = 0;
		this._head = new Node(0, null, null);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Node node = _head.next;
		String sep = "";
		while (node != null) {
			sb.append(sep).append("(").append(node.waketime).append(",")
			.append(node.agent).append(")");
			node = node.next;
			sep = ";";
		}
		sb.append("]");
		return (sb.toString());
	}
	public double currentTime() {
		return this._currentTime;
	}
	public void enqueue(double waketime, Agent thing) {
		if (waketime < _currentTime)
			throw new IllegalArgumentException();
		Node prevElement = _head;
		while ((prevElement.next != null) &&
				(prevElement.next.waketime <= waketime)) {
			prevElement = prevElement.next;
		}
		Node newElement = new Node(waketime, thing, prevElement.next);
		prevElement.next = newElement;
		_size++;
	}
	@Override
	public void run(double duration) {
		double endtime = _currentTime + duration;
		while ((!isEmpty()) && (_head.next.waketime <= endtime)) {
			_currentTime = _head.next.waketime;
			dequeue().run();
			super.setChanged();
		    super.notifyObservers();
		}
		_currentTime = endtime;
	}
	Agent dequeue(){
		if (_size < 1)
			throw new java.util.NoSuchElementException();
		Agent rval = _head.next.agent;
		_head.next = _head.next.next;
		_size--;
		return rval;
	}
	public int getSize(){ //remove public modifier - changed for testing purpose only
		return this._size;
	}
	public boolean isEmpty(){ //remove public modifier - changed for testing purpose only
		return this.getSize() == 0;
	}

}
