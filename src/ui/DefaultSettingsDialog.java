package ui;

import java.awt.Color;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author Albin Hjalmas.
 */
public class DefaultSettingsDialog extends javax.swing.JDialog {

    private double traceWidth, ringDiam, holeDiam, compSpacing;

    /**
     * Creates new form NewProjectDialog
     */
    public DefaultSettingsDialog(java.awt.Frame parent, boolean modal,
            double traceWidth, double ringDiam, double holeDiam, double compSpacing) {
        super(parent, modal);
        initComponents();
        this.traceWidth = traceWidth;
        widthTxtField.setText(Double.toString(traceWidth));
        this.ringDiam = ringDiam;
        ringDiamTxtField.setText(Double.toString(ringDiam));
        this.holeDiam = holeDiam;
        holeDiamTxtField.setText(Double.toString(holeDiam));
        this.compSpacing = compSpacing;
        spacingTxtField.setText(Double.toString(compSpacing));
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
        spacingTxtField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        widthTxtField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        ringDiamTxtField = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        holeDiamTxtField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(200, 0), new java.awt.Dimension(200, 0), new java.awt.Dimension(200, 32767));
        cancelBtn = new javax.swing.JButton();
        okBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Default Settings");
        setAlwaysOnTop(true);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Component Spacing [mm]"));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));
        jPanel2.add(spacingTxtField);

        getContentPane().add(jPanel2);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Trace Width [mm]"));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));
        jPanel1.add(widthTxtField);

        getContentPane().add(jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Pad Dimensions [mm]"));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

        ringDiamTxtField.setBorder(javax.swing.BorderFactory.createTitledBorder("Annular Ring Diameter"));
        ringDiamTxtField.setName(""); // NOI18N
        jPanel3.add(ringDiamTxtField);
        jPanel3.add(filler1);

        holeDiamTxtField.setBorder(javax.swing.BorderFactory.createTitledBorder("Hole Diameter"));
        jPanel3.add(holeDiamTxtField);

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

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
        try {
            compSpacing = Double.parseDouble(spacingTxtField.getText());
            traceWidth = Double.parseDouble(widthTxtField.getText());
            ringDiam = Double.parseDouble(ringDiamTxtField.getText());
            holeDiam = Double.parseDouble(holeDiamTxtField.getText());
            spacingTxtField.setForeground(Color.black);
            widthTxtField.setForeground(Color.black);
            ringDiamTxtField.setForeground(Color.black);
            holeDiamTxtField.setForeground(Color.black);
            setVisible(false);
        } catch (NumberFormatException e) {
            spacingTxtField.setForeground(Color.red);
            widthTxtField.setForeground(Color.red);
            ringDiamTxtField.setForeground(Color.red);
            holeDiamTxtField.setForeground(Color.red);
            JOptionPane.showMessageDialog(this,
                    "Incorrect PC-board measurements!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }//GEN-LAST:event_okBtnActionPerformed

    public double getTraceWidth() {
        return traceWidth;
    }
    
    public double getRingDiam() {
        return ringDiam;
    }
    
    public double getHoleDiam() {
        return holeDiam;
    }
    
    public double getCompSpacing() {
        return compSpacing;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JTextField holeDiamTxtField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton okBtn;
    private javax.swing.JTextField ringDiamTxtField;
    private javax.swing.JTextField spacingTxtField;
    private javax.swing.JTextField widthTxtField;
    // End of variables declaration//GEN-END:variables
}
