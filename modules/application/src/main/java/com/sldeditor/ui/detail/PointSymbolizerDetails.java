/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2016, SCISYS UK Limited
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sldeditor.ui.detail;

import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class PointSymbolizerDetails allows a user to configure point symbolizer data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class PointSymbolizerDetails extends StandardPanel implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public PointSymbolizerDetails(FunctionNameInterface functionManager)
    {
        super(PointSymbolizerDetails.class, functionManager);
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {

        readConfigFile(this, "Point.xml");
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        if(selectedSymbol != null)
        {
            Symbolizer symbolizer = selectedSymbol.getSymbolizer();
            populateStandardData(symbolizer);
        }

        updateSymbol();
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if(!Controller.getInstance().isPopulating())
        {
            StandardData standardData = getStandardData();

            String geometryFieldName = null;
            Expression geometryField = getFilterFactory().property(geometryFieldName);

            PointSymbolizer pointSymbolizer = (PointSymbolizer)SelectedSymbol.getInstance().getSymbolizer();

            if(pointSymbolizer != null)
            {
                pointSymbolizer.setName(standardData.name);
                pointSymbolizer.setDescription(standardData.description);
                pointSymbolizer.setUnitOfMeasure(standardData.unit);

                if((geometryField != null) && (geometryField.toString() != null) && !geometryField.toString().isEmpty())
                {
                    pointSymbolizer.setGeometry(geometryField);
                }

                this.fireUpdateSymbol();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.ui.detail.config.xml.FieldIdEnum)
     */
    @Override
    public void dataChanged(FieldId changedField) {
        updateSymbol();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.BasePanel#addRenderer(com.sldeditor.render.iface.RenderSymbolInterface)
     */
    @Override
    public void addRenderer(RenderSymbolInterface renderer) {
        super.addRenderer(renderer);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager()
    {
        return this.fieldConfigManager;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }
}