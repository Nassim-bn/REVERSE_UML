package fr.univrouen.umlreverse.ui.view.util;

public abstract class Gestures {
	/**
	 * The drag context that will correspond to this gesture.
	 */
	protected DragContext runningDrag;
	/**
	 * The pannable canvas that will correpond to this gesture.
	 */
	protected PannableCanvas canvas;
	
	class DragContext {
		/**
		 * The x position of mice.
		 */
		private double mouseX;
		/**
		 * The y position of mice.
		 */
		private double mouseY;
		/**
		 * The x translate valor.
		 */
		private double translateX;
		/**
		 * The y translate valor.
		 */
		private double translateY;

		/**
		 * Constructor of drag context.
		 * @param mx X position of mice
		 * @param my y position of mice
         * @param tx x translate valor
         * @param ty y translate valor
         */
		public DragContext(double mx, double my, double tx, double ty) {
			mouseX = mx;
			mouseY = my;
			translateX = tx;
			translateY = ty;
		}

		/**
		 * Getter of the x position of mice.
		 * @return double
         */
		public double getMouseX() {
			return mouseX;
		}

		/**
		 * Getter of the y position of mice.
		 * @return double
         */
		public double getMouseY() {
			return mouseY;
		}

		/**
		 * Getter of the x translate valor.
		 * @return double
         */
		public double getTranslateX() {
			return translateX;
		}

		/**
		 * Getter of the y translate valor.
		 * @return double
         */
		public double getTranslateY() {
			return translateY;
		}
	}
}
