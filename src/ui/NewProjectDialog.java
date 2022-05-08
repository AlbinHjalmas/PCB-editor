package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author albin
 */
public class NewProjectDialog extends javax.swing.JDialog {

    private File projectPath;
    private double width, height;
    private boolean cancelState;

    /**
     * Creates new form NewProjectDialog
     */
    public NewProjectDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        projectPath = new File("");

        locationTxtField.setText(projectPath.getAbsolutePath());

        this.setFocusable(true);
        
        projectNameTxtField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                projectPath = new File(
                        projectPath.getAbsolutePath()
                                .replaceFirst(projectPath.getName(), "") 
                                + "/" + projectNameTxtField.getText() + e.getKeyChar() + ".pcb");
                locationTxtField.setText(projectPath.getAbsolutePath());
            }
        });
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancelBtnActionPerformed(null);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        projectNameTxtField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        locationTxtField = new javax.swing.JTextField();
        browseBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        widthTxtField = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        heightTxtField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(200, 0), new java.awt.Dimension(200, 0), new java.awt.Dimension(200, 32767));
        cancelBtn = new javax.swing.JButton();
        okBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Project");
        setAlwaysOnTop(true);
        setLocation(new java.awt.Point(600, 600));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Project Name"));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));
        jPanel2.add(projectNameTxtField);

        getContentPane().add(jPanel2);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Project Location"));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));
        jPanel1.add(locationTxtField);

        browseBtn.setText("Browse");
        browseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBtnActionPerformed(evt);
            }
        });
        jPanel1.add(browseBtn);

        getContentPane().add(jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Board Dimensions [mm]"));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

        widthTxtField.setBorder(javax.swing.BorderFactory.createTitledBorder("Width"));
        widthTxtField.setName(""); // NOI18N
        jPanel3.add(widthTxtField);
        jPanel3.add(filler1);

        heightTxtField.setBorder(javax.swing.BorderFactory.createTitledBorder("Height"));
        jPanel3.add(heightTxtField);

        getContentPane().add(jPanel3);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jPanel4.add(filler2);

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        jPanel4.add(cancelBtn);

        okBtn.setText("OK");
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });
        jPanel4.add(okBtn);

        getContentPane().add(jPanel4);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void browseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBtnActionPerformed
        JFileChooser fs = new JFileChooser();
        fs.setSelectedFile(projectPath);
        fs.showSaveDialog(this);
        projectPath = fs.getSelectedFile();
        
        if(!projectPath.getAbsolutePath().matches(".+.pcb")) {
            projectPath = new File(projectPath.getAbsolutePath() + ".pcb");
        }
        
        locationTxtField.setText(projectPath.getPath());
        projectNameTxtField.setText(projectPath.getName().replaceAll(".pcb", ""));
    }//GEN-LAST:event_browseBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        cancelState = true;
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
        try {
            if (projectNameTxtField.getText().length() == 0) {
                JOptionPane.showMessageDialog(this,
                        "You must provide a project name!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            width = Double.parseDouble(widthTxtField.getText());
            height = Double.parseDouble(heightTxtField.getText());
            widthTxtField.setForeground(Color.black);
            heightTxtField.setForeground(Color.black);
            cancelState = false;
            setVisible(false);
        } catch (NumberFormatException e) {
            widthTxtField.setForeground(Color.red);
            heightTxtField.setForeground(Color.red);
            JOptionPane.showMessageDialog(this,
                    "Incorrect PC-board measurements!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }//GEN-LAST:event_okBtnActionPerformed

    public boolean wasCancelled() {
        return cancelState;
    }
    
    public String getProjectName() {
        return projectNameTxtField.getText();
    }
    
    public File getProjectFile() {
        return projectPath;
    }
    
    public double getBoardWidth() {
        return width;
    }
    
    public double getBoardHeight() {
        return height;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JTextField heightTxtField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField locationTxtField;
    private javax.swing.JButton okBtn;
    private javax.swing.JTextField projectNameTxtField;
    private javax.swing.JTextField widthTxtField;
    // End of variables declaration//GEN-END:variables
}
