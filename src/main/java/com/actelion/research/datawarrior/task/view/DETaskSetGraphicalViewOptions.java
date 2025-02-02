/*
 * Copyright 2017 Idorsia Pharmaceuticals Ltd., Hegenheimermattweg 91, CH-4123 Allschwil, Switzerland
 *
 * This file is part of DataWarrior.
 * 
 * DataWarrior is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * DataWarrior is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with DataWarrior.
 * If not, see http://www.gnu.org/licenses/.
 *
 * @author Thomas Sander
 */

package com.actelion.research.datawarrior.task.view;

import com.actelion.research.datawarrior.DEMainPane;
import com.actelion.research.datawarrior.task.DEColorPanel;
import com.actelion.research.gui.hidpi.HiDPIHelper;
import com.actelion.research.table.model.CompoundTableListHandler;
import com.actelion.research.table.view.*;
import com.actelion.research.util.DoubleFormat;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Properties;


public class DETaskSetGraphicalViewOptions extends DETaskAbstractSetViewOptions implements KeyListener {
	public static final String TASK_NAME = "Set Graphical View Options";

	private static final String PROPERTY_FONT_SIZE = "fontSize";
	private static final String PROPERTY_FONT_SIZE_MODE = "fontSizeMode";
	private static final String PROPERTY_HIDE_GRID = "hideGrid";	// allowed: x,y,true,false
	private static final String PROPERTY_HIDE_LEGEND = "hideLegend";
	private static final String PROPERTY_HIDE_SCALE = "hideScale";	// allowed: x,y,true,false
	private static final String PROPERTY_SCALE_STYLE = "scaleStyle";	// allowed: x,y,true,false
	private static final String PROPERTY_CROSSHAIR_MODE = "crosshair";
	private static final String PROPERTY_DYNAMIC_SCALE = "dynamicScale";
	private static final String PROPERTY_SHOW_EMPTY = "showEmpty";
	private static final String PROPERTY_GLOBAL_EXCLUSION = "globalExclusion";
	private static final String PROPERTY_IGNORE_FILTERS = "ignoreFilters";
	private static final String PROPERTY_FAST_RENDERING = "fastRendering";
	private static final String PROPERTY_DRAW_BOX_OUTLINE = "boxOutline";
	private static final String PROPERTY_DRAW_MARKER_OUTLINE = "markerOutline";
	private static final String PROPERTY_SCATTERPLOT_MARGIN = "scatterplotMargin";
	private static final String PROPERTY_DEFAULT_DATA_COLOR = "defaultDataColor";
	private static final String PROPERTY_MISSING_DATA_COLOR = "missingDataColor";
	private static final String PROPERTY_BACKGROUND_COLOR = "backgroundColor";
	private static final String PROPERTY_LABEL_BACKGROUND_COLOR = "labelBackgroundColor";
	private static final String PROPERTY_GRAPH_FACE_COLOR = "graphFaceColor";
	private static final String PROPERTY_TITLE_BACKGROUND_COLOR = "titleBGColor";
	private static final String PROPERTY_EXCLUSION_LIST = "exclusionList";
	private static final String PROPERTY_AUTO_ZOOM_COLUMN = "autoZoomColumn";
	private static final String PROPERTY_AUTO_ZOOM_FACTOR = "autoZoomValue";

	private static final String ITEM_AUTO_ZOOM_NONE = "<none>";

	private JComboBox       mComboBoxScaleMode,mComboBoxGridMode,mComboBoxCrossHairMode,mComboBoxExclusionList,
							mComboBoxFontSizeMode;
	private JCheckBox[]     mCheckBoxAutoZoom;
	private JComboBox[]     mComboBoxAutoZoomColumn;
	private JSlider			mSliderScaleFontSize;
	private JCheckBox	    mCheckBoxHideLegend,mCheckBoxShowNaN,mCheckBoxGlobalExclusion,mCheckBoxFastRendering,
							mCheckBoxShowArrowTips,mCheckBoxIgnoreFilters,mCheckBoxDrawBoxOutline,mCheckBoxDrawMarkerOutline,
							mCheckBoxDynamicScale,mCheckBoxScatterplotMargin;
	private DEColorPanel    mDefaultDataColorPanel,mMissingDataColorPanel,mBackgroundColorPanel,mLabelBackgroundColorPanel,
							mGraphFaceColorPanel,mTitleBackgroundPanel;
	private JTextField[]    mTextFieldAutoZoomFactor;

	/**
	 * @param owner
	 * @param mainPane
	 * @param view null or view that is interactively updated
	 */
	public DETaskSetGraphicalViewOptions(Frame owner, DEMainPane mainPane, VisualizationPanel view) {
		super(owner, mainPane, view);
		}

	@Override
	public String getViewQualificationError(CompoundTableView view) {
		return (view instanceof VisualizationPanel) ? null : "Graphical view properties can only be applied to 2D- and 3D-Views.";
		}

	@Override
	public boolean isViewTaskWithoutConfiguration() {
		return false;
		}

	@Override
	public OTHER_VIEWS getOtherViewMode() {
		return OTHER_VIEWS.GRAPHICAL;
		}

	@Override
	public JComponent createViewOptionContent() {
		JPanel scalePanel = new JPanel();
		int gap = HiDPIHelper.scale(8);
		double[][] sizeScalePanel = { {gap, TableLayout.FILL, TableLayout.PREFERRED, gap, TableLayout.PREFERRED, TableLayout.FILL, gap},
									  {gap, TableLayout.PREFERRED, gap, TableLayout.PREFERRED, gap, TableLayout.PREFERRED, gap,
											TableLayout.PREFERRED, gap/2, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, gap/2} };
		scalePanel.setLayout(new TableLayout(sizeScalePanel));

		scalePanel.add(new JLabel("Show scales:"), "2,1");
		mComboBoxScaleMode = new JComboBox((!hasInteractiveView() || getInteractiveVisualization() instanceof JVisualization2D) ?
							JVisualization2D.SCALE_MODE_TEXT : JVisualization3D.SCALE_MODE_TEXT);
		mComboBoxScaleMode.addActionListener(this);
		scalePanel.add(mComboBoxScaleMode, "4,1");

		scalePanel.add(new JLabel("Show grid:"), "2,3");
		mComboBoxGridMode = new JComboBox((!hasInteractiveView() || getInteractiveVisualization() instanceof JVisualization2D) ?
				JVisualization2D.GRID_MODE_TEXT : JVisualization3D.GRID_MODE_TEXT);
		mComboBoxGridMode.addActionListener(this);
		scalePanel.add(mComboBoxGridMode, "4,3");

		if (!hasInteractiveView() || getInteractiveVisualization() instanceof JVisualization2D) {
			scalePanel.add(new JLabel("Show crosshair:"), "2,5");
			mComboBoxCrossHairMode = new JComboBox(JVisualization2D.CROSSHAIR_MODE_TEXT);
			mComboBoxCrossHairMode.addActionListener(this);
			scalePanel.add(mComboBoxCrossHairMode, "4,5");
			}

		mComboBoxFontSizeMode = new JComboBox(JVisualization.FONT_SIZE_MODE_TEXT);
		mComboBoxFontSizeMode.addActionListener(this);

		mSliderScaleFontSize = new JSlider(JSlider.HORIZONTAL, 0, 150, 50);
		mSliderScaleFontSize.setPreferredSize(new Dimension(HiDPIHelper.scale(150), mSliderScaleFontSize.getPreferredSize().height));
		mSliderScaleFontSize.addChangeListener(this);
		scalePanel.add(mComboBoxFontSizeMode, "2,7");
		scalePanel.add(mSliderScaleFontSize, "4,7");

		mCheckBoxHideLegend = new JCheckBox("Hide legend", false);
		mCheckBoxHideLegend.addActionListener(this);
		scalePanel.add(mCheckBoxHideLegend, "2,9,4,9");

		mCheckBoxDynamicScale = new JCheckBox("Adapt scale dynamically (bar charts only)", false);
		mCheckBoxDynamicScale.addActionListener(this);
		scalePanel.add(mCheckBoxDynamicScale, "2,10,4,10");

		if (!hasInteractiveView() || getInteractiveVisualization() instanceof JVisualization2D) {
			mCheckBoxShowArrowTips = new JCheckBox("Show arrow tips", false);
			mCheckBoxShowArrowTips.addActionListener(this);
			scalePanel.add(mCheckBoxShowArrowTips, "2,11,4,11");
			}

		JPanel staticColorPanel = new JPanel();
		double[][] sizeStaticColorPanel = { {gap, TableLayout.FILL, TableLayout.PREFERRED, gap, HiDPIHelper.scale(64),
											 gap+gap/2, TableLayout.PREFERRED, TableLayout.FILL, gap},
											{gap, TableLayout.PREFERRED, gap, TableLayout.PREFERRED, gap, TableLayout.PREFERRED, gap, TableLayout.PREFERRED,
											 gap, TableLayout.PREFERRED, gap, TableLayout.PREFERRED} };
		staticColorPanel.setLayout(new TableLayout(sizeStaticColorPanel));

		int colorIndex = 0;
		mDefaultDataColorPanel = addColorChooser("Default marker color:", "changeDDC", 1+2*colorIndex++,
				VisualizationColor.cDefaultDataColor, staticColorPanel);
		mMissingDataColorPanel = addColorChooser("Missing data color:", "changeMDC", 1+2*colorIndex++,
				VisualizationColor.cMissingDataColor, staticColorPanel);
		mBackgroundColorPanel = addColorChooser("Background color:", "changeBGC", 1+2*colorIndex++,
				JVisualization.DEFAULT_LABEL_BACKGROUND, staticColorPanel);
		mLabelBackgroundColorPanel = addColorChooser("Label Background:", "changeLBG", 1+2*colorIndex++,
				Color.WHITE, staticColorPanel);
		if (!hasInteractiveView() || getInteractiveVisualization().isSplitViewConfigured())
			mTitleBackgroundPanel = addColorChooser("Splitting title area:", "changeTAC", 1+2*colorIndex++,
					JVisualization.DEFAULT_TITLE_BACKGROUND, staticColorPanel);
		if (!hasInteractiveView() || getInteractiveVisualization() instanceof JVisualization3D)
			mGraphFaceColorPanel = addColorChooser("Graph face color:", "changeGFC", 1+2*colorIndex++,
					new Color(0x00FFFFFF & JVisualization3D.DEFAULT_GRAPH_FACE_COLOR), staticColorPanel);

		JPanel rowHidingPanel = new JPanel();
		double[][] sizeRowHidingPanel = { {gap, TableLayout.PREFERRED, gap, TableLayout.PREFERRED, TableLayout.FILL, gap},
										  {gap, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, gap, TableLayout.PREFERRED, gap} };
		rowHidingPanel.setLayout(new TableLayout(sizeRowHidingPanel));
		mCheckBoxShowNaN = new JCheckBox("Show empty values", false);
		mCheckBoxShowNaN.addActionListener(this);
		rowHidingPanel.add(mCheckBoxShowNaN, "1,1,3,1");
		mCheckBoxGlobalExclusion = new JCheckBox("Hide invisible rows in other views", true);
		mCheckBoxGlobalExclusion.addActionListener(this);
		rowHidingPanel.add(mCheckBoxGlobalExclusion, "1,2,3,2");
		mCheckBoxIgnoreFilters = new JCheckBox("Ignore global filters", false);
		mCheckBoxIgnoreFilters.addActionListener(this);
		rowHidingPanel.add(mCheckBoxIgnoreFilters, "1,3,3,3");
		mComboBoxExclusionList = new JComboBox();
		mComboBoxExclusionList.addItem(CompoundTableListHandler.LISTNAME_NONE);
		for (int i = 0; i<getTableModel().getListHandler().getListCount(); i++)
			mComboBoxExclusionList.addItem(getTableModel().getListHandler().getListName(i));
		if (!isInteractive())
			mComboBoxExclusionList.setEditable(true);
		mComboBoxExclusionList.addActionListener(this);
		rowHidingPanel.add(new JLabel("Hide rows not in list:"), "1,5");
		rowHidingPanel.add(mComboBoxExclusionList, "3,5");

		JPanel autoZoomPanel = new JPanel();
		double[][] sizeAutoZoomPanel = { {gap, TableLayout.FILL, TableLayout.PREFERRED, gap, TableLayout.PREFERRED, gap, TableLayout.PREFERRED, TableLayout.FILL, gap},
				{gap, TableLayout.PREFERRED, gap, TableLayout.PREFERRED, gap/2, TableLayout.PREFERRED, gap/2, TableLayout.PREFERRED, gap/2, TableLayout.PREFERRED, gap} };
		autoZoomPanel.setLayout(new TableLayout(sizeAutoZoomPanel));
		autoZoomPanel.add(new JLabel("Define axes to automatically zoom to center the reference row"), "1,1,7,1");
		autoZoomPanel.add(new JLabel("Zoom-factor:"), "4,3");
		autoZoomPanel.add(new JLabel("Also zoom by:"), "6,3");
		int dimensions = hasInteractiveView() ? getInteractiveVisualization().getDimensionCount() : 3;
		mCheckBoxAutoZoom = new JCheckBox[dimensions];
		mTextFieldAutoZoomFactor = new JTextField[dimensions];
		mComboBoxAutoZoomColumn = new JComboBox[dimensions];
		for (int i=0; i<dimensions; i++) {
			mCheckBoxAutoZoom[i] = new JCheckBox(dimensions==3 ? (i==0?"X:":i==1?"Y:":"Z:") : (i==0?"Horizontal:":"Vertical:"));
			mCheckBoxAutoZoom[i].addActionListener(this);
			mTextFieldAutoZoomFactor[i] = new JTextField(4);
			mTextFieldAutoZoomFactor[i].addKeyListener(this);
			mComboBoxAutoZoomColumn[i] = new JComboBox<String>();
			if (!isInteractive())
				mComboBoxAutoZoomColumn[i].setEditable(true);
			mComboBoxAutoZoomColumn[i].addItem(ITEM_AUTO_ZOOM_NONE);
			for (int j=0; j<getTableModel().getTotalColumnCount(); j++)
				if (getTableModel().isColumnTypeDouble(j) && !getTableModel().isColumnTypeDate(j))
					mComboBoxAutoZoomColumn[i].addItem(getTableModel().getColumnTitle(j));
			mComboBoxAutoZoomColumn[i].addActionListener(this);
			autoZoomPanel.add(mCheckBoxAutoZoom[i], "2,"+(5+i*2));
			autoZoomPanel.add(mTextFieldAutoZoomFactor[i], "4,"+(5+i*2));
			autoZoomPanel.add(mComboBoxAutoZoomColumn[i], "6,"+(5+i*2));
			}

		JPanel renderPanel = new JPanel();
		double[][] sizeRenderPanel = { {gap, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, gap},
									   {gap, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, gap} };
		renderPanel.setLayout(new TableLayout(sizeRenderPanel));
		mCheckBoxFastRendering = new JCheckBox("Fast rendering / lower quality", false);
		mCheckBoxFastRendering.addActionListener(this);
		renderPanel.add(mCheckBoxFastRendering, "2,1");
		mCheckBoxScatterplotMargin = new JCheckBox("Use margin in scatter plots", true);
		mCheckBoxScatterplotMargin.addActionListener(this);
		renderPanel.add(mCheckBoxScatterplotMargin, "2,2");
		if (!hasInteractiveView() || getInteractiveVisualization() instanceof JVisualization2D) {
			mCheckBoxDrawBoxOutline = new JCheckBox("Draw bar/pie/box outlines", false);
			mCheckBoxDrawBoxOutline.addActionListener(this);
			renderPanel.add(mCheckBoxDrawBoxOutline, "2,3");
			mCheckBoxDrawMarkerOutline = new JCheckBox("Draw marker outlines", false);
			mCheckBoxDrawMarkerOutline.addActionListener(this);
			renderPanel.add(mCheckBoxDrawMarkerOutline, "2,4");
			}

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setToolTipText("");
		tabbedPane.add(scalePanel, "Scales");
		tabbedPane.add(staticColorPanel, "Colors");
		tabbedPane.add(rowHidingPanel, "Row hiding");
		tabbedPane.add(autoZoomPanel, "Auto zoom");
		tabbedPane.add(renderPanel, "Rendering");
		tabbedPane.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));

		return tabbedPane;
		}

	private DEColorPanel addColorChooser(String text, String actionCommand, int position, Color color, JPanel backgroundPanel) {
		backgroundPanel.add(new JLabel(text), "2,"+position);
		DEColorPanel colorPanel = new DEColorPanel(color);
		backgroundPanel.add(colorPanel, "4,"+position);
		JButton button = new JButton("Change");
		button.setActionCommand(actionCommand);
		button.addActionListener(this);
		backgroundPanel.add(button, "6,"+position);
		return colorPanel;
		}

	@Override
	public void setDialogToDefault() {
		mComboBoxScaleMode.setSelectedIndex(0);
		mComboBoxGridMode.setSelectedIndex(0);
		if (mComboBoxCrossHairMode != null)
			mComboBoxCrossHairMode.setSelectedIndex(0);
		mComboBoxFontSizeMode.setSelectedIndex(0);
		mSliderScaleFontSize.setValue(50);
		mCheckBoxHideLegend.setSelected(false);
		mCheckBoxDynamicScale.setSelected(true);
		if (mCheckBoxShowArrowTips != null)
			mCheckBoxShowArrowTips.setSelected(false);
		mCheckBoxShowNaN.setSelected(false);
		mCheckBoxGlobalExclusion.setSelected(true);
		mCheckBoxFastRendering.setSelected(false);
		mCheckBoxScatterplotMargin.setSelected(true);
		if (mCheckBoxDrawBoxOutline != null)
			mCheckBoxDrawBoxOutline.setSelected(false);
		if (mCheckBoxDrawMarkerOutline != null)
			mCheckBoxDrawMarkerOutline.setSelected(true);

		if (mDefaultDataColorPanel != null)
			mDefaultDataColorPanel.setColor(VisualizationColor.cDefaultDataColor);
		if (mMissingDataColorPanel != null)
			mMissingDataColorPanel.setColor(VisualizationColor.cMissingDataColor);
		if (mBackgroundColorPanel != null)
			mBackgroundColorPanel.setColor(Color.WHITE);
		if (mLabelBackgroundColorPanel != null)
			mLabelBackgroundColorPanel.setColor(JVisualization.DEFAULT_LABEL_BACKGROUND);
		if (mGraphFaceColorPanel != null)
			mGraphFaceColorPanel.setColor(new Color(0x00FFFFFF & JVisualization3D.DEFAULT_GRAPH_FACE_COLOR));
		if (mTitleBackgroundPanel != null)
			mTitleBackgroundPanel.setColor(JVisualization.DEFAULT_TITLE_BACKGROUND);
		mComboBoxExclusionList.setSelectedIndex(0);

		for (JCheckBox cb:mCheckBoxAutoZoom)
			cb.setSelected(false);
		for (JTextField tf:mTextFieldAutoZoomFactor)
			tf.setText("2.0");
		for (JComboBox cb:mComboBoxAutoZoomColumn)
			cb.setSelectedItem(ITEM_AUTO_ZOOM_NONE);
		}

	@Override
	public void setDialogToConfiguration(Properties configuration) {
		double fontSize = 1.0;
		try { fontSize = Double.parseDouble(configuration.getProperty(PROPERTY_FONT_SIZE, "1.0")); } catch (NumberFormatException nfe) {}
		int scaleMode = findListIndex(configuration.getProperty(PROPERTY_HIDE_SCALE), JVisualization.SCALE_MODE_CODE, 0);
		mComboBoxScaleMode.setSelectedIndex(scaleMode<mComboBoxScaleMode.getItemCount() ? scaleMode : 0);
		int gridMode = findListIndex(configuration.getProperty(PROPERTY_HIDE_GRID), JVisualization.GRID_MODE_CODE, 0);
		mComboBoxGridMode.setSelectedIndex(gridMode<mComboBoxGridMode.getItemCount() ? gridMode : 0);
		int crossHairMode = findListIndex(configuration.getProperty(PROPERTY_CROSSHAIR_MODE), JVisualization2D.CROSSHAIR_MODE_CODE, 0);
		if (mComboBoxCrossHairMode != null)
			mComboBoxCrossHairMode.setSelectedIndex(crossHairMode<mComboBoxCrossHairMode.getItemCount() ? crossHairMode : 0);
		mComboBoxFontSizeMode.setSelectedIndex(findListIndex(configuration.getProperty(PROPERTY_FONT_SIZE_MODE), JVisualization2D.FONT_SIZE_MODE_CODE, 0));
		mSliderScaleFontSize.setValue(50+(int)(50.0*Math.log(fontSize)));
		mCheckBoxHideLegend.setSelected("true".equals(configuration.getProperty(PROPERTY_HIDE_LEGEND)));
		mCheckBoxDynamicScale.setSelected(!"false".equals(configuration.getProperty(PROPERTY_DYNAMIC_SCALE)));
		if (mCheckBoxShowArrowTips != null)
			mCheckBoxShowArrowTips.setSelected(JVisualization.SCALE_STYLE_CODE[JVisualization.cScaleStyleArrows].equals(configuration.getProperty(PROPERTY_SCALE_STYLE)));
		mCheckBoxShowNaN.setSelected("true".equals(configuration.getProperty(PROPERTY_SHOW_EMPTY)));
		mCheckBoxIgnoreFilters.setSelected("true".equals(configuration.getProperty(PROPERTY_IGNORE_FILTERS)));
		mCheckBoxGlobalExclusion.setSelected(!"false".equals(configuration.getProperty(PROPERTY_GLOBAL_EXCLUSION)));
		mCheckBoxFastRendering.setSelected("true".equals(configuration.getProperty(PROPERTY_FAST_RENDERING)));
		mCheckBoxScatterplotMargin.setSelected(!"0".equals(configuration.getProperty(PROPERTY_SCATTERPLOT_MARGIN)));
		if (mCheckBoxDrawBoxOutline != null)
			mCheckBoxDrawBoxOutline.setSelected("true".equals(configuration.getProperty(PROPERTY_DRAW_BOX_OUTLINE)));
		if (mCheckBoxDrawMarkerOutline != null)
			mCheckBoxDrawMarkerOutline.setSelected(!"false".equals(configuration.getProperty(PROPERTY_DRAW_MARKER_OUTLINE)));

		if (mDefaultDataColorPanel != null)
			try { mDefaultDataColorPanel.setColor(Color.decode(configuration.getProperty(PROPERTY_DEFAULT_DATA_COLOR))); } catch (NumberFormatException nfe) {}
		if (mMissingDataColorPanel != null)
			try { mMissingDataColorPanel.setColor(Color.decode(configuration.getProperty(PROPERTY_MISSING_DATA_COLOR))); } catch (NumberFormatException nfe) {}
		if (mBackgroundColorPanel != null)
			try { mBackgroundColorPanel.setColor(Color.decode(configuration.getProperty(PROPERTY_BACKGROUND_COLOR))); } catch (NumberFormatException nfe) {}
		if (mLabelBackgroundColorPanel != null)
			try { mLabelBackgroundColorPanel.setColor(Color.decode(configuration.getProperty(PROPERTY_LABEL_BACKGROUND_COLOR))); } catch (NumberFormatException nfe) {}
		if (mTitleBackgroundPanel != null) {
			String colorText = configuration.getProperty(PROPERTY_TITLE_BACKGROUND_COLOR);
			try { mTitleBackgroundPanel.setColor((colorText == null) ? JVisualization.DEFAULT_TITLE_BACKGROUND : Color.decode(colorText)); } catch (NumberFormatException nfe) {}
			}
		if (mGraphFaceColorPanel != null) {
			String colorText = configuration.getProperty(PROPERTY_GRAPH_FACE_COLOR);
			try { mGraphFaceColorPanel.setColor((colorText == null) ? new Color(0x00FFFFFF & JVisualization3D.DEFAULT_GRAPH_FACE_COLOR) : Color.decode(colorText)); } catch (NumberFormatException nfe) {}
			}
		String exclusionListName =  configuration.getProperty(PROPERTY_EXCLUSION_LIST, "");
		mComboBoxExclusionList.setSelectedItem(exclusionListName.length() == 0 ? CompoundTableListHandler.LISTNAME_NONE : exclusionListName);

		String[] azf = configuration.getProperty(PROPERTY_AUTO_ZOOM_FACTOR, "").split(";", -1);
		String[] azc = configuration.getProperty(PROPERTY_AUTO_ZOOM_COLUMN, "").split(";", -1);
		for (int i=0; i<mTextFieldAutoZoomFactor.length; i++) {
			String text = azf[i<azf.length ? i : 0];
			mCheckBoxAutoZoom[i].setSelected(text.length() != 0);
			mTextFieldAutoZoomFactor[i].setText(text.length() == 0 ? "2.0" : text);
			text = azc[i<azc.length ? i : 0].replaceAll("&#59", ";");
			mComboBoxAutoZoomColumn[i].setSelectedItem(text.length() == 0 ? ITEM_AUTO_ZOOM_NONE
				: isInteractive() ? getTableModel().getColumnTitle(getTableModel().findColumn(text)) : text);
			}
		}

	@Override
	public void addDialogConfiguration(Properties configuration) {
		configuration.setProperty(PROPERTY_FONT_SIZE_MODE, JVisualization2D.FONT_SIZE_MODE_CODE[mComboBoxFontSizeMode.getSelectedIndex()]);
		float size = (float)Math.exp((double)(mSliderScaleFontSize.getValue()-50)/50.0);
		configuration.setProperty(PROPERTY_FONT_SIZE, ""+size);
		configuration.setProperty(PROPERTY_HIDE_SCALE, JVisualization.SCALE_MODE_CODE[mComboBoxScaleMode.getSelectedIndex()]);
		configuration.setProperty(PROPERTY_HIDE_GRID, JVisualization.GRID_MODE_CODE[mComboBoxGridMode.getSelectedIndex()]);
		if (mComboBoxCrossHairMode != null)
			configuration.setProperty(PROPERTY_CROSSHAIR_MODE, JVisualization2D.CROSSHAIR_MODE_CODE[mComboBoxCrossHairMode.getSelectedIndex()]);
		configuration.setProperty(PROPERTY_HIDE_LEGEND, mCheckBoxHideLegend.isSelected() ? "true" : "false");
		configuration.setProperty(PROPERTY_DYNAMIC_SCALE, mCheckBoxDynamicScale.isSelected() ? "true" : "false");
		if (mCheckBoxShowArrowTips != null)
			configuration.setProperty(PROPERTY_SCALE_STYLE, JVisualization.SCALE_STYLE_CODE[mCheckBoxShowArrowTips.isSelected() ?
				JVisualization.cScaleStyleArrows : JVisualization.cScaleStyleFrame]);
		configuration.setProperty(PROPERTY_SHOW_EMPTY, mCheckBoxShowNaN.isSelected() ? "true" : "false");
		configuration.setProperty(PROPERTY_IGNORE_FILTERS, mCheckBoxIgnoreFilters.isSelected() ? "true" : "false");
		configuration.setProperty(PROPERTY_GLOBAL_EXCLUSION, mCheckBoxGlobalExclusion.isSelected() ? "true" : "false");
		configuration.setProperty(PROPERTY_FAST_RENDERING, mCheckBoxFastRendering.isSelected() ? "true" : "false");
		configuration.setProperty(PROPERTY_SCATTERPLOT_MARGIN, mCheckBoxScatterplotMargin.isSelected() ?
				DoubleFormat.toString(JVisualization.getDefaultScatterplotMargin()) : "0");
		if (mCheckBoxDrawBoxOutline != null)
			configuration.setProperty(PROPERTY_DRAW_BOX_OUTLINE, mCheckBoxDrawBoxOutline.isSelected() ? "true" : "false");
		if (mCheckBoxDrawMarkerOutline != null)
			configuration.setProperty(PROPERTY_DRAW_MARKER_OUTLINE, mCheckBoxDrawMarkerOutline.isSelected() ? "true" : "false");

		if (mDefaultDataColorPanel != null)
			configuration.setProperty(PROPERTY_DEFAULT_DATA_COLOR, ""+mDefaultDataColorPanel.getColor().getRGB());
		if (mMissingDataColorPanel != null)
			configuration.setProperty(PROPERTY_MISSING_DATA_COLOR, ""+mMissingDataColorPanel.getColor().getRGB());
		if (mBackgroundColorPanel != null)
			configuration.setProperty(PROPERTY_BACKGROUND_COLOR, ""+mBackgroundColorPanel.getColor().getRGB());
		if (mLabelBackgroundColorPanel != null)
			configuration.setProperty(PROPERTY_LABEL_BACKGROUND_COLOR, ""+mLabelBackgroundColorPanel.getColor().getRGB());
		if (mTitleBackgroundPanel != null)
			configuration.setProperty(PROPERTY_TITLE_BACKGROUND_COLOR, ""+mTitleBackgroundPanel.getColor().getRGB());
		if (mGraphFaceColorPanel != null)
			configuration.setProperty(PROPERTY_GRAPH_FACE_COLOR, ""+mGraphFaceColorPanel.getColor().getRGB());
		if (mComboBoxExclusionList.getSelectedIndex() != 0)
			configuration.setProperty(PROPERTY_EXCLUSION_LIST, (String)mComboBoxExclusionList.getSelectedItem());

		StringBuilder sb = new StringBuilder();
		for (int i=0; i<mCheckBoxAutoZoom.length; i++) {
			if (i != 0)
				sb.append(";");
			if (mCheckBoxAutoZoom[i].isSelected())
				sb.append(mTextFieldAutoZoomFactor[i].getText());
			}
		if (sb.length() > 2) {
			configuration.setProperty(PROPERTY_AUTO_ZOOM_FACTOR, sb.toString());
			sb.setLength(0);
			for (int i=0; i<mComboBoxAutoZoomColumn.length; i++) {
				if (i != 0)
					sb.append(";");
				if (mCheckBoxAutoZoom[i].isSelected()
				 && !ITEM_AUTO_ZOOM_NONE.equals(mComboBoxAutoZoomColumn[i].getSelectedItem()))
					sb.append(getTableModel().getColumnTitleNoAlias((String)mComboBoxAutoZoomColumn[i].getSelectedItem()).replaceAll(";", "&#59"));
				}
			if (sb.length() > 2)
				configuration.setProperty(PROPERTY_AUTO_ZOOM_COLUMN, sb.toString());
			}
		}

	@Override
	public void addViewConfiguration(CompoundTableView view, Properties configuration) {
		JVisualization visualization = ((VisualizationPanel)view).getVisualization();
		configuration.setProperty(PROPERTY_FONT_SIZE_MODE, JVisualization2D.FONT_SIZE_MODE_CODE[visualization.getFontSizeMode()]);
		configuration.setProperty(PROPERTY_FONT_SIZE, ""+ visualization.getFontSize());
		configuration.setProperty(PROPERTY_HIDE_SCALE, JVisualization.SCALE_MODE_CODE[visualization.getScaleMode()]);
		configuration.setProperty(PROPERTY_HIDE_GRID, JVisualization.GRID_MODE_CODE[visualization.getGridMode()]);
		if (visualization instanceof JVisualization2D)
			configuration.setProperty(PROPERTY_CROSSHAIR_MODE, JVisualization2D.CROSSHAIR_MODE_CODE[((JVisualization2D)visualization).getCrossHairMode()]);
		configuration.setProperty(PROPERTY_HIDE_LEGEND, visualization.isLegendSuppressed() ? "true" : "false");
		configuration.setProperty(PROPERTY_DYNAMIC_SCALE, visualization.isDynamicScale() ? "true" : "false");
		if (visualization instanceof JVisualization2D)
			configuration.setProperty(PROPERTY_SCALE_STYLE, JVisualization.SCALE_STYLE_CODE[visualization.getScaleStyle()]);
		configuration.setProperty(PROPERTY_SHOW_EMPTY, visualization.getShowNaNValues() ? "true" : "false");
		configuration.setProperty(PROPERTY_GLOBAL_EXCLUSION, visualization.getAffectGlobalExclusion() ? "true" : "false");
		configuration.setProperty(PROPERTY_IGNORE_FILTERS, visualization.isIgnoreGlobalExclusion() ? "true" : "false");
		configuration.setProperty(PROPERTY_FAST_RENDERING, visualization.isFastRendering() ? "true" : "false");
		configuration.setProperty(PROPERTY_SCATTERPLOT_MARGIN, DoubleFormat.toString(visualization.getScatterPlotMargin()));
		if (visualization instanceof JVisualization2D) {
			configuration.setProperty(PROPERTY_DRAW_BOX_OUTLINE, ((JVisualization2D)visualization).isDrawBarPieBoxOutline() ? "true" : "false");
			configuration.setProperty(PROPERTY_DRAW_MARKER_OUTLINE, ((JVisualization2D)visualization).isDrawMarkerOutline() ? "true" : "false");
			}

		configuration.setProperty(PROPERTY_DEFAULT_DATA_COLOR, ""+ visualization.getMarkerColor().getDefaultDataColor().getRGB());
		configuration.setProperty(PROPERTY_MISSING_DATA_COLOR, ""+ visualization.getMarkerColor().getMissingDataColor().getRGB());
		configuration.setProperty(PROPERTY_BACKGROUND_COLOR, ""+ visualization.getViewBackground().getRGB());
		configuration.setProperty(PROPERTY_LABEL_BACKGROUND_COLOR, ""+ visualization.getDefaultLabelBackground().getRGB());
		configuration.setProperty(PROPERTY_TITLE_BACKGROUND_COLOR, ""+ visualization.getTitleBackground().getRGB());
		if (visualization instanceof JVisualization3D)
			configuration.setProperty(PROPERTY_GRAPH_FACE_COLOR, ""+((JVisualization3D) visualization).getGraphFaceColor().getRGB());
		if (visualization.getLocalExclusionList() != CompoundTableListHandler.LISTINDEX_NONE)
			configuration.setProperty(PROPERTY_EXCLUSION_LIST, getTableModel().getListHandler().getListName(visualization.getLocalExclusionList()));

		float[] azf = ((VisualizationPanel)view).getAutoZoomFactor();
		if (azf != null) {
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<azf.length; i++) {
				if (i != 0)
					sb.append(";");
				if (azf[i] != 0f)
					sb.append(azf[i]);
				}
			configuration.setProperty(PROPERTY_AUTO_ZOOM_FACTOR, sb.toString());
			int[] azc = ((VisualizationPanel)view).getAutoZoomColumn();
			if (azc != null) {
				sb.setLength(0);
				for (int i=0; i<azc.length; i++) {
					if (i != 0)
						sb.append(";");
					if (azc[i] != -1)
						sb.append(getTableModel().getColumnTitleNoAlias(azc[i]).replaceAll(";", "&#59"));
					}
				if (sb.length() > 2)
					configuration.setProperty(PROPERTY_AUTO_ZOOM_COLUMN, sb.toString());
				}
			}
		}

	@Override
	public String getTaskName() {
		return TASK_NAME;
		}

	@Override
	public boolean isViewConfigurationValid(CompoundTableView view, Properties configuration) {
		return true;
		}

	@Override
	public void applyConfiguration(CompoundTableView view, Properties configuration, boolean isAdjusting) {
		if (!(view instanceof VisualizationPanel))
			return;

		JVisualization v = ((VisualizationPanel)view).getVisualization();
		int scaleMode = findListIndex(configuration.getProperty(PROPERTY_HIDE_SCALE), JVisualization.SCALE_MODE_CODE, 0);
		if ((v instanceof JVisualization2D && scaleMode<JVisualization2D.SCALE_MODE_TEXT.length)
		 || (v instanceof JVisualization3D && scaleMode<JVisualization3D.SCALE_MODE_TEXT.length))
			v.setScaleMode(scaleMode);
		int gridMode = findListIndex(configuration.getProperty(PROPERTY_HIDE_GRID), JVisualization.GRID_MODE_CODE, 0);
		if ((v instanceof JVisualization2D && gridMode<JVisualization2D.GRID_MODE_TEXT.length)
		 || (v instanceof JVisualization3D && gridMode<JVisualization3D.GRID_MODE_TEXT.length))
			v.setGridMode(gridMode);
		int crossHairMode = findListIndex(configuration.getProperty(PROPERTY_CROSSHAIR_MODE), JVisualization2D.CROSSHAIR_MODE_CODE, 0);
		if (v instanceof JVisualization2D)
			((JVisualization2D)v).setCrossHairMode(crossHairMode);
		float fontSize = 1f;
		try {
			fontSize = Float.parseFloat(configuration.getProperty(PROPERTY_FONT_SIZE, "1.0"));
			int fontSizeMode = findListIndex(configuration.getProperty(PROPERTY_FONT_SIZE_MODE), JVisualization2D.FONT_SIZE_MODE_CODE, 0);
			v.setFontSize(fontSize, fontSizeMode, isAdjusting);
			}
		catch (NumberFormatException nfe) {}
		v.setSuppressLegend("true".equals(configuration.getProperty(PROPERTY_HIDE_LEGEND)));
		v.setDynamicScale(!"false".equals(configuration.getProperty(PROPERTY_DYNAMIC_SCALE)));
		if (v instanceof JVisualization2D)
			v.setScaleStyle(findListIndex(configuration.getProperty(PROPERTY_SCALE_STYLE), JVisualization.SCALE_STYLE_CODE, JVisualization.cScaleStyleFrame));
		v.setShowNaNValues("true".equals(configuration.getProperty(PROPERTY_SHOW_EMPTY)));
		v.setAffectGlobalExclusion(!"false".equals(configuration.getProperty(PROPERTY_GLOBAL_EXCLUSION)));
		v.setIgnoreGlobalExclusion("true".equals(configuration.getProperty(PROPERTY_IGNORE_FILTERS)));
		v.setFastRendering("true".equals(configuration.getProperty(PROPERTY_FAST_RENDERING)));
		try {
			String margin = configuration.getProperty(PROPERTY_SCATTERPLOT_MARGIN);
			if (margin != null)
				v.setScatterPlotMargin(Float.parseFloat(margin));
			}
		catch (NumberFormatException nfe) {}
		if (v instanceof JVisualization2D) {
			((JVisualization2D)v).setDrawBoxOutline("true".equals(configuration.getProperty(PROPERTY_DRAW_BOX_OUTLINE)));
			((JVisualization2D)v).setDrawMarkerOutline(!"false".equals(configuration.getProperty(PROPERTY_DRAW_MARKER_OUTLINE)));
			}

		String ddc = configuration.getProperty(PROPERTY_DEFAULT_DATA_COLOR);
		if (ddc != null)
			try { v.getMarkerColor().setDefaultDataColor(Color.decode(ddc)); } catch (NumberFormatException nfe) {}
		String mdc = configuration.getProperty(PROPERTY_MISSING_DATA_COLOR);
		if (mdc != null)
			try { v.getMarkerColor().setMissingDataColor(Color.decode(mdc)); } catch (NumberFormatException nfe) {}
		String bgc = configuration.getProperty(PROPERTY_BACKGROUND_COLOR);
		if (bgc != null)
			try { v.setViewBackground(Color.decode(bgc)); } catch (NumberFormatException nfe) {}
		String lbg = configuration.getProperty(PROPERTY_LABEL_BACKGROUND_COLOR);
		if (lbg != null)
			try { v.setDefaultLabelBackground(Color.decode(lbg)); } catch (NumberFormatException nfe) {}
		String tbc = configuration.getProperty(PROPERTY_TITLE_BACKGROUND_COLOR);
		if (tbc != null)
			try { v.setTitleBackground(Color.decode(tbc)); } catch (NumberFormatException nfe) {}
		String gfc = configuration.getProperty(PROPERTY_GRAPH_FACE_COLOR);
		if (gfc != null && v instanceof JVisualization3D)
			try { ((JVisualization3D)v).setGraphFaceColor(Color.decode(gfc)); } catch (NumberFormatException nfe) {}
		v.setLocalExclusionList(getTableModel().getListHandler().getListIndex(configuration.getProperty(PROPERTY_EXCLUSION_LIST)));

		float[] azf = null;
		int[] azc = null;
		String azfText = configuration.getProperty(PROPERTY_AUTO_ZOOM_FACTOR);
		if (azfText != null) {
			String[] azft = azfText.split(";", -1);
			int dimensions = ((VisualizationPanel)view).getDimensionCount();
			azf = new float[dimensions];
			for (int i=0; i<dimensions; i++) {
				String text = azft[i<azft.length ? i : 0];
				if (text.length() != 0)
					try { azf[i] = Float.parseFloat(text); } catch (NumberFormatException nfe) {}
				}
			azc = new int[dimensions];
			Arrays.fill(azc, -1);
			String azcText = configuration.getProperty(PROPERTY_AUTO_ZOOM_COLUMN);
			if (azcText != null) {
				String[] azct = azcText.split(";", -1);
				for (int i=0; i<dimensions; i++) {
					String text = azct[i<azct.length ? i : 0];
					if (text.length() != 0)
						azc[i] = getTableModel().findColumn(text.replaceAll("&#59", ";"));
					}
				}
			}
		((VisualizationPanel)view).setAutoZoom(azf, azc, true);
		}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "changeDDC") {
			Color newColor = JColorChooser.showDialog(getDialog(), "Select Default Marker Color", mDefaultDataColorPanel.getColor());
			if (newColor == null || newColor.equals(mDefaultDataColorPanel.getColor()))
				return;
			mDefaultDataColorPanel.setColor(newColor);
			}
		else if (e.getActionCommand() == "changeMDC") {
			Color newColor = JColorChooser.showDialog(getDialog(), "Select Missing Data Color", mMissingDataColorPanel.getColor());
			if (newColor == null || newColor.equals(mMissingDataColorPanel.getColor()))
				return;
			mMissingDataColorPanel.setColor(newColor);
			}
		else if (e.getActionCommand() == "changeBGC") {
			Color newColor = JColorChooser.showDialog(getDialog(), "Select Background Color", mBackgroundColorPanel.getColor());
			if (newColor == null || newColor.equals(mBackgroundColorPanel.getColor()))
				return;
			mBackgroundColorPanel.setColor(newColor);
			}
		else if (e.getActionCommand() == "changeLBG") {
			Color newColor = JColorChooser.showDialog(getDialog(), "Select Label Background Color", mLabelBackgroundColorPanel.getColor());
			if (newColor == null || newColor.equals(mLabelBackgroundColorPanel.getColor()))
				return;
			mLabelBackgroundColorPanel.setColor(newColor);
			}
		else if (e.getActionCommand() == "changeGFC") {
			Color newColor = JColorChooser.showDialog(getDialog(), "Select Graph Face Color", mGraphFaceColorPanel.getColor());
			if (newColor == null || newColor.equals(mGraphFaceColorPanel.getColor()))
				return;
			mGraphFaceColorPanel.setColor(newColor);
			}
		else if (e.getActionCommand() == "changeTAC") {
			Color newColor = JColorChooser.showDialog(getDialog(), "Select Splitting Title Area Color", mTitleBackgroundPanel.getColor());
			if (newColor == null || newColor.equals(mTitleBackgroundPanel.getColor()))
				return;
			mTitleBackgroundPanel.setColor(newColor);
			}

		super.actionPerformed(e);
		}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		float value = Float.NaN;
		JTextField source = (JTextField)e.getSource();
		int index = -1;
		for (int i=0; i<mTextFieldAutoZoomFactor.length; i++) {
			if (source == mTextFieldAutoZoomFactor[i]) {
				index = i;
				break;
				}
			}
		if (index != -1) {
			try { value = Float.parseFloat(source.getText()); } catch (NumberFormatException nfe) {}
			boolean isValidZoomValue = !Float.isNaN(value)
					&& (mComboBoxAutoZoomColumn[index].getSelectedIndex() != 0 || (value > 1f));

			source.setBackground(isValidZoomValue ? UIManager.getColor("TextField.background") : Color.RED);

			if (isValidZoomValue)
				update(false);
			}
		}

	@Override
	public void enableItems() {
		for (int i=0; i<mCheckBoxAutoZoom.length; i++) {
			mTextFieldAutoZoomFactor[i].setEnabled(mCheckBoxAutoZoom[i].isSelected());
			mComboBoxAutoZoomColumn[i].setEnabled(mCheckBoxAutoZoom[i].isSelected());
			}
		}
	}
