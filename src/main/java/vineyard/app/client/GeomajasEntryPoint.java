/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package vineyard.app.client;

import java.util.ArrayList;
import java.util.List;

import vineyard.app.client.i18n.Translation;
import vineyard.app.client.pages.AbstractTab;
import vineyard.app.client.pages.FeatureListGridPage;
import vineyard.app.client.pages.SearchPage;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.LoadingScreen;
import org.geomajas.gwt.client.widget.LocaleSelect;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;
import org.geomajas.gwt.client.widget.ScaleSelect;
import org.geomajas.gwt.client.widget.Toolbar;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

import org.geomajas.plugin.staticsecurity.client.LoginWindow;
import org.geomajas.plugin.staticsecurity.client.Authentication;
import org.geomajas.plugin.staticsecurity.client.event.LoginFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LoginHandler;
import org.geomajas.plugin.staticsecurity.client.event.LoginSuccessEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutHandler;
import org.geomajas.plugin.staticsecurity.client.event.LogoutSuccessEvent;
import com.smartgwt.client.util.BooleanCallback;

import com.google.gwt.core.client.GWT;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this
 * application.
 * 
 * @author Pieter De Graef
 */
public class GeomajasEntryPoint implements EntryPoint {

    private MapWidget map;

    private OverviewMap overviewMap;

    private Legend legend;

    private TabSet tabSet = new TabSet();

    private List<AbstractTab> tabs = new ArrayList<AbstractTab>();

    public GeomajasEntryPoint() {
    }

    public void onModuleLoad() {
        // Used for i18n in configuration files:
        I18nProvider.setLookUp(GWT.<ConstantsWithLookup>create(Translation.class));

        VLayout mainLayout = new VLayout();
        mainLayout.setWidth100();
        mainLayout.setHeight100();

        // ---------------------------------------------------------------------
        // Top bar:
        // ---------------------------------------------------------------------
        ToolStrip topBar = new ToolStrip();
        topBar.setHeight(33);
        topBar.setWidth100();
        topBar.addSpacer(6);
        
        Img icon = new Img("[ISOMORPHIC]/geomajas/geomajas_desktopicon_small.png");
        icon.setSize(24);
        topBar.addMember(icon);
        topBar.addSpacer(6);

        Label title = new Label("Vineyards");
        title.setStyleName("sgwtTitle");
        title.setWidth(300);
        topBar.addMember(title);

        final Label userLabel = new Label();
        userLabel.setHeight(20);
        userLabel.setWidth100();
        userLabel.setPadding(3);
        userLabel.setBorder("1px solid #A0A0A0");
        topBar.addMember(userLabel);

        final LoginHandler lh = new LoginHandler() {
                public void onLoginFailure(LoginFailureEvent event) {
                    GWT.log("Failure", null);
                }
                public void onLoginSuccess(LoginSuccessEvent event) {
                    GWT.log("Success", null);
                    userLabel.setContents("Logged in with: " + Authentication.getInstance().getUserId());
                }
            };
        
        final LogoutHandler lo = new LogoutHandler() {
                public void onLogoutFailure(LogoutFailureEvent event) {
                }
                public void onLogoutSuccess(LogoutSuccessEvent event) {
                    userLabel.setContents("No user is logged in.");
                }
            };
        Authentication.getInstance().addLoginHandler(lh);
        Authentication.getInstance().addLogoutHandler(lo);

        IButton loginButton = new IButton("Log in");
        loginButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    LoginWindow lw = new LoginWindow(lh);
                    lw.setAutoCenter(true);
                    lw.setIsModal(true);
                    lw.setShowModalMask(true);
                    lw.setShowMinimizeButton(false);
                    lw.setShowMaximizeButton(false);
                    lw.show();
                }
            });
        topBar.addMember(loginButton);

        Authentication.getInstance().login("josh", "josh", null);
                
        topBar.addFill();
        topBar.addMember(new LocaleSelect("English"));

        mainLayout.addMember(topBar);

        HLayout layout = new HLayout();
        layout.setWidth100();
        layout.setHeight100();
        layout.setMembersMargin(5);
        layout.setMargin(5);

        // ---------------------------------------------------------------------
        // Create the left-side (map and tabs):
        // ---------------------------------------------------------------------
        map = new MapWidget("sampleFeaturesMap", "gwt-simple");
        final Toolbar toolbar = new Toolbar(map);
        toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

        VLayout mapLayout = new VLayout();
        mapLayout.setShowResizeBar(true);
        mapLayout.setResizeBarTarget("mytabs");
        mapLayout.addMember(toolbar);
        mapLayout.addMember(map);
        mapLayout.setHeight("65%");
        tabSet.setTabBarPosition(Side.TOP);
        tabSet.setWidth100();
        tabSet.setHeight("35%");
        tabSet.setID("mytabs");

        VLayout leftLayout = new VLayout();
        leftLayout.setShowEdges(true);
        leftLayout.addMember(mapLayout);
        leftLayout.addMember(tabSet);

        layout.addMember(leftLayout);

        // ---------------------------------------------------------------------
        // Create the right-side (overview map, layer-tree, legend):
        // ---------------------------------------------------------------------
        final SectionStack sectionStack = new SectionStack();
        sectionStack.setShowEdges(true);
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setCanReorderSections(true);
        sectionStack.setCanResizeSections(false);
        sectionStack.setSize("250px", "100%");

        // Overview map layout:
        SectionStackSection section1 = new SectionStackSection("Overview map");
        section1.setExpanded(true);
        overviewMap = new OverviewMap("sampleOverviewMap", "gwt-simple", map, true, true);
        section1.addItem(overviewMap);
        sectionStack.addSection(section1);

        // LayerTree layout:
        SectionStackSection section2 = new SectionStackSection("Layer tree");
        section2.setExpanded(true);
        LayerTree layerTree = new LayerTree(map);
        section2.addItem(layerTree);
        sectionStack.addSection(section2);

        // Legend layout:
        SectionStackSection section3 = new SectionStackSection("Legend");
        section3.setExpanded(true);
        legend = new Legend(map.getMapModel());
        section3.addItem(legend);
        sectionStack.addSection(section3);

        // Putting the right side layouts together:
        layout.addMember(sectionStack);

        // ---------------------------------------------------------------------
        // Bottom left: Add tabs here:
        // ---------------------------------------------------------------------
        FeatureListGridPage page1 = new FeatureListGridPage(map);
        addTab(new SearchPage(map, tabSet, page1.getTable()));
        addTab(page1);

        // ---------------------------------------------------------------------
        // Finally draw everything:
        // ---------------------------------------------------------------------
        mainLayout.addMember(layout);
        mainLayout.draw();

        // Install a loading screen
        // This only works if the application initially shows a map with at least 1 vector layer:
        LoadingScreen loadScreen = new LoadingScreen(map, "Hello...");
        loadScreen.draw();

        // Then initialize:
        initialize();
    }

    private void addTab(AbstractTab tab) {
        tabSet.addTab(tab);
        tabs.add(tab);
    }

    private void initialize() {
        legend.setHeight(200);
        overviewMap.setHeight(200);

        for (AbstractTab tab : tabs) {
            tab.initialize();
        }
    }
}
