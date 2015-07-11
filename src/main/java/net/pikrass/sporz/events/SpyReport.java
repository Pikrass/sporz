package net.pikrass.sporz.events;

import net.pikrass.sporz.*;

import java.util.TreeSet;
import java.util.SortedSet;

public class SpyReport extends Event {
	private Player origin;
	private Player target;
	private SortedSet<Line> result;

	public SpyReport(Player origin, Player target) {
		this.origin = origin;
		this.target = target;
		this.result = new TreeSet<Line>();
	}

	public void addLine(Line line) {
		this.result.add(line);
	}

	public Player getOrigin() {
		return origin;
	}

	public Player getTarget() {
		return target;
	}

	public SortedSet<Line> getResult() {
		return result;
	}


	public enum LineType {
		MUTATION,
		PARALYSIS,
		HEALING,
		PSYCHOANALYSIS,
		SEQUENCING
	}

	public class Line implements Comparable<Line> {
		private LineType type;
		private String name;

		public Line(LineType type) {
			this.type = type;
			this.name = null;
		}

		public Line(LineType type, String name) {
			this.type = type;
			this.name = name;
		}

		public LineType getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Line))
				return false;

			Line l = (Line)o;

			return l.getType().equals(type) &&
				(name == null && l.getName() == null
				 || l.getName().equals(name));
		}

		@Override
		public int compareTo(Line o) {
			if(this.equals(o))
				return 0;

			if(!this.type.equals(o.getType()))
				return this.type.compareTo(o.getType());

			return this.name.compareTo(o.getName());
		}
	}
}
