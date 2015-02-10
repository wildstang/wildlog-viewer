package org.wildstang.sdlogreader;

import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class GraphPanelViewController implements MouseWheelListener {

	private static final double deltaZoomFactor = 0.1;

	DataPanel[] dataPanels;

	private double zoomFactor = 1.0;
	private int scrollPosition = 0;

	public GraphPanelViewController(DataPanel[] dataPanels) {
		this.dataPanels = dataPanels;
	}

	private void recalculateAndUpdate() {
		long startTimestamp, endTimestamp;
		
		double deltaTimestamp = (Deserialize.endTimestamp - Deserialize.startTimestamp) / zoomFactor;
		for (int i = 0; i < dataPanels.length; i++) {
			dataPanels[i].updateGraphView((long) Deserialize.startTimestamp, (long) (Deserialize.startTimestamp + deltaTimestamp));
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getModifiers() == InputEvent.CTRL_MASK) {
			zoomFactor += e.getPreciseWheelRotation() * deltaZoomFactor;
		} else {
			scrollPosition = Scroller.scrollyPolly.getValue() + e.getWheelRotation();
			Scroller.scrollyPolly.setValue(scrollPosition);
		}
		recalculateAndUpdate();

	}

}
