package ui;

import graphPrimitives.Component;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import pcbEditor.CoordinateListener;
import pcbEditor.DesignArea;
import pcbPart.Footprint;
import pcbPart.Part;
import pcbPrimitives.ThPad;

/**
 *
 * @author Albin Hjalmas.
 */
public class Editor extends javax.swing.JFrame implements CoordinateListener {

    private final String aboutDialogContents; // The contents of the about dialog
    private File openProject; // The currently open project
    private DefaultTreeModel treeModel;
    
    // The design area
    private DesignArea da;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Set GTK+ look and feel.
        setLaf("Windows");

        Editor pcEdit = new Editor();
        pcEdit.setVisible(true);
    }

    /**
     * Creates new form Editor.
     */
    public Editor() {
        initComponents();
        setTitle("Pcb Editor 1.0 - Albin Hjälmås 2017");
        aboutDialogContents = "Created by Albin Hjälmås\n"
                + "    2017 - 05 - 03";
        cursorBtn.setSelected(true);
        
        // Create tree
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Parts");
        createTreeNodes(top);
        treeModel = new DefaultTreeModel(top);
        tree.setModel(treeModel);
    }

    /**
     * Create the parts in the parts tree.
     * @param top the top of the tree.
     */
    private void createTreeNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode part = null;
        
        // Create passive components.
        category = new DefaultMutableTreeNode("Passive", true);
        top.add(category);
        part = new DefaultMutableTreeNode(new Part(
                "resistor length 12.7mm, hole 0.8mm",
                "resistor length 12.7mm, hole 0.8mm",
                new Footprint(new Point2D.Double(0,0),
                        new ThPad(new Point2D.Double(0,-6.35 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true), 
                        new ThPad(new Point2D.Double(0,6.35 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true)),
                2.5 * 50, 14.4 * 50
        ), false);
        category.add(part);
        part = new DefaultMutableTreeNode(new Part(
                "resistor length 7.62mm, hole 0.6mm",
                "resistor length 7.62mm, hole 0.6mm",
                new Footprint(new Point2D.Double(0,0),
                        new ThPad(new Point2D.Double(0,-3.81 * 50), 1.3 * 50, 0.6 * 50, Color.gray, true), 
                        new ThPad(new Point2D.Double(0,3.81 * 50), 1.3 * 50, 0.6 * 50, Color.gray, true)),
                2 * 50, 9.12 * 50
        ));
        category.add(part);
        
        part = new DefaultMutableTreeNode(new Part(
                "capacitor leg spacing 12.7mm, hole 0.8mm",
                "capacitor leg spacing 12.7mm, hole 0.8mm",
                new Footprint(new Point2D.Double(0,0),
                        new ThPad(new Point2D.Double(0,-6.35 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true), 
                        new ThPad(new Point2D.Double(0,6.35 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true)),
                14.4 * 50, 14.4 * 50
        ), false);
        category.add(part);
        part = new DefaultMutableTreeNode(new Part(
                "capacitor leg spacing 7.62mm, hole 0.6mm",
                "capacitor leg spacing 7.62mm, hole 0.6mm",
                new Footprint(new Point2D.Double(0,0),
                        new ThPad(new Point2D.Double(0,-3.81 * 50), 1.3 * 50, 0.6 * 50, Color.gray, true), 
                        new ThPad(new Point2D.Double(0,3.81 * 50), 1.3 * 50, 0.6 * 50, Color.gray, true)),
                9.12 * 50, 9.12 * 50
        ), false);
        category.add(part);
        
        // Create active components
        category = new DefaultMutableTreeNode("Active", true);
        top.add(category);
        part = new DefaultMutableTreeNode(new Part(
                "8 legged op-amp, 0.8mm holes",
                "8 legged op-amp, 0.8mm holes",
                new Footprint(new Point2D.Double(0,0),
                        new ThPad(new Point2D.Double(-3.81 * 50, 3.81 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true), 
                        new ThPad(new Point2D.Double(-3.81 * 50, 1.27 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true),
                        new ThPad(new Point2D.Double(-3.81 * 50, -1.27 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true),
                        new ThPad(new Point2D.Double(-3.81 * 50, -3.81 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true),
                        new ThPad(new Point2D.Double(3.81 * 50, -3.81 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true), 
                        new ThPad(new Point2D.Double(3.81 * 50, -1.27 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true),
                        new ThPad(new Point2D.Double(3.81 * 50, 1.27 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true),
                        new ThPad(new Point2D.Double(3.81 * 50, 3.81 * 50), 1.5 * 50, 0.8 * 50, Color.gray, true)),
                9.32 * 50, 10.36 * 50
        ), false);
        category.add(part);
    }
    
    /**
     * Overridden from CoordinateListener interface. This listener callback is
     * called from the current designArea.
     *
     * @param p The new coordinates.
     */
    @Override
    public void CoordinateChanged(Point2D.Double p) {
        xCoordLbl.setText(String.format("%.2f", p.x));
        yCoordLbl.setText(String.format("%.2f", p.y));
    }

    /**
     * Set the look and feel.
     *
     * @param laf
     */
    private static void setLaf(String laf) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (laf.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolBarBtnGroup = new javax.swing.ButtonGroup();
        toolBar = new javax.swing.JToolBar();
        cursorBtn = new javax.swing.JToggleButton();
        moveBtn = new javax.swing.JToggleButton();
        routeBtn = new javax.swing.JToggleButton();
        runDrcBtn = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        treeView = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        jPanel4 = new javax.swing.JPanel();
        placeBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        centerScrollPane = new javax.swing.JScrollPane();
        centerPanel = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        xLbl = new javax.swing.JLabel();
        xCoordLbl = new javax.swing.JLabel();
        yLbl = new javax.swing.JLabel();
        yCoordLbl = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newProjectMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        openProjectMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        saveMenuItem = new javax.swing.JMenuItem();
        closeMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        defaultSettingsMenuItem = new javax.swing.JMenuItem();
        selectedSettingsMenuItem = new javax.swing.JMenuItem();
        netManagerMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        bomMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PCB Editor");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setExtendedState(6);
        setName("mainFrame"); // NOI18N
        setSize(new java.awt.Dimension(1920, 1080));

        toolBar.setRollover(true);

        toolBarBtnGroup.add(cursorBtn);
        cursorBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/data/Cursor-icon.png"))); // NOI18N
        cursorBtn.setFocusable(false);
        cursorBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cursorBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cursorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cursorBtnActionPerformed(evt);
            }
        });
        toolBar.add(cursorBtn);

        toolBarBtnGroup.add(moveBtn);
        moveBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/data/Cursor-Move-icon.png"))); // NOI18N
        moveBtn.setFocusable(false);
        moveBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        moveBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        moveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveBtnActionPerformed(evt);
            }
        });
        toolBar.add(moveBtn);

        toolBarBtnGroup.add(routeBtn);
        routeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/data/Route-icon.png"))); // NOI18N
        routeBtn.setFocusable(false);
        routeBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        routeBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        routeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routeBtnActionPerformed(evt);
            }
        });
        toolBar.add(routeBtn);

        runDrcBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/data/Play-Music-icon.png"))); // NOI18N
        runDrcBtn.setFocusable(false);
        runDrcBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runDrcBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        runDrcBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runDrcBtnActionPerformed(evt);
            }
        });
        toolBar.add(runDrcBtn);

        getContentPane().add(toolBar, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setContinuousLayout(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Components"));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        treeView.setViewportView(tree);

        jPanel1.add(treeView);

        jPanel4.setToolTipText("Places the currently selected component from the component tree.");
        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 39));

        placeBtn.setText("Place");
        placeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placeBtnActionPerformed(evt);
            }
        });
        jPanel4.add(placeBtn);

        jPanel1.add(jPanel4);

        jSplitPane1.setTopComponent(jPanel1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        centerScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Editor"));
        centerScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        centerScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        centerScrollPane.setWheelScrollingEnabled(false);

        centerPanel.setLayout(new java.awt.BorderLayout());
        centerScrollPane.setViewportView(centerPanel);

        jPanel2.add(centerScrollPane, java.awt.BorderLayout.CENTER);

        jToolBar1.setRollover(true);

        xLbl.setText("x = ");
        jToolBar1.add(xLbl);

        xCoordLbl.setText("102.55");
        jToolBar1.add(xCoordLbl);

        yLbl.setText("      y = ");
        jToolBar1.add(yLbl);

        yCoordLbl.setText("1023.55");
        jToolBar1.add(yCoordLbl);

        jPanel2.add(jToolBar1, java.awt.BorderLayout.PAGE_END);

        jSplitPane1.setRightComponent(jPanel2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");

        newProjectMenuItem.setText("New Project");
        newProjectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProjectMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newProjectMenuItem);
        fileMenu.add(jSeparator1);

        openProjectMenuItem.setText("Open Project");
        openProjectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openProjectMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openProjectMenuItem);

        saveAsMenuItem.setText("Save As");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(jSeparator2);

        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        closeMenuItem.setText("Close");
        closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(closeMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        defaultSettingsMenuItem.setText("Default Settings");
        defaultSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultSettingsMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(defaultSettingsMenuItem);

        selectedSettingsMenuItem.setText("Selected Settings");
        selectedSettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedSettingsMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(selectedSettingsMenuItem);

        netManagerMenuItem.setText("Net Manager");
        netManagerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netManagerMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(netManagerMenuItem);

        menuBar.add(editMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        jMenu1.setText("Tools");

        bomMenuItem.setText("BOM");
        bomMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bomMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(bomMenuItem);

        menuBar.add(jMenu1);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        JOptionPane.showMessageDialog(this, aboutDialogContents,
                "About", JOptionPane.INFORMATION_MESSAGE);
        this.requestFocus();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void cursorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cursorBtnActionPerformed
        if(openProject == null) return;
        da.setCursorState(DesignArea.CURSOR_NORMAL);
        this.requestFocus();
    }//GEN-LAST:event_cursorBtnActionPerformed

    private void moveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveBtnActionPerformed
        if(openProject == null) return;
        da.setCursorState(DesignArea.CURSOR_MOVE);
        this.requestFocus();
    }//GEN-LAST:event_moveBtnActionPerformed

    private void routeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routeBtnActionPerformed
        if(openProject == null) return;
        da.setCursorState(DesignArea.CURSOR_ROUTE);
        this.requestFocus();
    }//GEN-LAST:event_routeBtnActionPerformed

    private void openProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openProjectMenuItemActionPerformed
        try {
            JFileChooser fs = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("PCB Project", "pcb");
            fs.setFileFilter(filter);
            fs.showOpenDialog(this);
            
            // Check if a valid file was selected
            if (fs.getSelectedFile() == null || !fs.getSelectedFile().getAbsolutePath().matches(".+.pcb")) {
                return;
            }

            // Check if there is a currently open project
            if (openProject != null) {
                closeMenuItemActionPerformed(evt);
            }

            openProject = fs.getSelectedFile();
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(openProject));
            da = (DesignArea) is.readObject();
            is.close();
            da.addCoordinateListener(this);

            // Add mouse listeners.
            da.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    da.mousePressedImpl(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    da.mouseReleasedImpl(e);
                }
            });
            da.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    da.mouseDraggedImpl(e);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    da.mouseMovedImpl(e);
                }
            });
            this.addKeyListener(da);
            centerPanel.add(da, BorderLayout.CENTER);
            cursorBtn.setSelected(true);

            revalidate();
            this.requestFocus();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_openProjectMenuItemActionPerformed

    private void newProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProjectMenuItemActionPerformed

        // Prompt user to decide whether or not to save
        // currently open project or not.
        if (openProject != null) { // There is a project open
            closeMenuItemActionPerformed(evt);
        }

        // Open a new project dialog
        NewProjectDialog np = new NewProjectDialog(this, true);
        np.setLocationRelativeTo(null);
        np.setVisible(true);
        if (np.wasCancelled()) {
            return;
        }

        // Create new DesignArea.
        openProject = np.getProjectFile();
        da = new DesignArea(np.getProjectName(), (int) (np.getBoardWidth() * 50.0),
                (int) (np.getBoardHeight() * 50.0));
        da.addCoordinateListener(this);
        this.addKeyListener(da);
        centerPanel.add(da, BorderLayout.CENTER);
        cursorBtn.setSelected(true);
        
        revalidate();
        this.requestFocus();
    }//GEN-LAST:event_newProjectMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        if (openProject == null) {
            JOptionPane.showMessageDialog(this, "No project is open!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            saveMenuItemActionPerformed(evt); // Save current project first
            JFileChooser fc = new JFileChooser(openProject);
            FileFilter filter = new FileNameExtensionFilter("PCB Project", "pcb");
            fc.setFileFilter(filter);
            fc.showSaveDialog(this);
            
            // Check that there was a new path created.
            if(fc.getSelectedFile() == null) {
                return;
            } else {
                openProject = fc.getSelectedFile();
            }
            
            if(!openProject.getAbsolutePath().matches(".+.pcb")) {
                openProject = new File(openProject.getAbsolutePath() + ".pcb");
            }
            da.setProjectName(openProject.getName().replace(".pcb", ""));
            saveMenuItemActionPerformed(evt); // Save the designarea into the new file
            this.requestFocus();
        }

    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        try {
            FileOutputStream fos = new FileOutputStream(openProject);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            SwingUtilities.invokeLater(() -> {
                try {
                    os.writeObject(da);
                    os.flush();
                    os.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Unable to save project!\n"
                            + ex.getMessage(),
                            "Save Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            this.requestFocus();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Unable to save project!\n"
                    + ex.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Unable to save project!\n"
                    + ex.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuItemActionPerformed
        // Prompt user to decide whether or not to save
        // currently open project or not.
        if (openProject != null) { // There is a project open
            int select = JOptionPane.showConfirmDialog(this, "Do you want to save current project?");
            switch (select) {
                case JOptionPane.CANCEL_OPTION:
                    return;
                case JOptionPane.OK_OPTION:
                    saveMenuItemActionPerformed(null);
                case JOptionPane.NO_OPTION:
                    centerPanel.remove(da);
                    this.removeKeyListener(da);
                    da = null;
                    openProject = null;
                    break;
            }
            revalidate();
            this.requestFocus();
        }
    }//GEN-LAST:event_closeMenuItemActionPerformed

    private void defaultSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultSettingsMenuItemActionPerformed
        if(openProject == null) return;
        da.showDefaultSettingsDialog();
        this.requestFocus();
    }//GEN-LAST:event_defaultSettingsMenuItemActionPerformed

    private void selectedSettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedSettingsMenuItemActionPerformed
        if(openProject == null) return;
        da.showSelectedSettingsDialog();
        this.requestFocus();
    }//GEN-LAST:event_selectedSettingsMenuItemActionPerformed

    private void placeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placeBtnActionPerformed
        try {
            
            if(openProject == null) {
                return;
            }
            
            // Can only place components when "Normal" cursor state is selected.
            if(da.getCursorState() != DesignArea.CURSOR_NORMAL) {
                JOptionPane.showMessageDialog(this, "You can only place components "
                        + "when \"Normal\" cursor state is selected!", "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            Part part = (Part) ((Part)((DefaultMutableTreeNode)tree.getSelectionModel()
                    .getSelectionPath().getLastPathComponent()).getUserObject()).clone();
            
            PlaceManager p = new PlaceManager(this, part, true);
            p.setLocationRelativeTo(null);
            p.pack();
            p.setVisible(true);
            
            // Check if place manager was cancelled
            if(p.wasCancelled()) {
                return;
            }
            
            da.placePart(part);
            this.requestFocus();
        } catch (ClassCastException | CloneNotSupportedException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_placeBtnActionPerformed

    private void netManagerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netManagerMenuItemActionPerformed
        if(openProject == null) return;
        da.showNetManager();
    }//GEN-LAST:event_netManagerMenuItemActionPerformed

    private void runDrcBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runDrcBtnActionPerformed
        if(openProject == null) {
            return;
        }
        
        da.runDesignRuleCheck();
    }//GEN-LAST:event_runDrcBtnActionPerformed

    private void bomMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bomMenuItemActionPerformed
        if(openProject == null) {
            return;
        }
        
        da.saveBOM();
    }//GEN-LAST:event_bomMenuItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem bomMenuItem;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JScrollPane centerScrollPane;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JToggleButton cursorBtn;
    private javax.swing.JMenuItem defaultSettingsMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JToggleButton moveBtn;
    private javax.swing.JMenuItem netManagerMenuItem;
    private javax.swing.JMenuItem newProjectMenuItem;
    private javax.swing.JMenuItem openProjectMenuItem;
    private javax.swing.JButton placeBtn;
    private javax.swing.JToggleButton routeBtn;
    private javax.swing.JButton runDrcBtn;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem selectedSettingsMenuItem;
    private javax.swing.JToolBar toolBar;
    private javax.swing.ButtonGroup toolBarBtnGroup;
    private javax.swing.JTree tree;
    private javax.swing.JScrollPane treeView;
    private javax.swing.JLabel xCoordLbl;
    private javax.swing.JLabel xLbl;
    private javax.swing.JLabel yCoordLbl;
    private javax.swing.JLabel yLbl;
    // End of variables declaration//GEN-END:variables
}
