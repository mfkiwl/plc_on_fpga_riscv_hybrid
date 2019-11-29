/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rv_fpga_plc_ide.helper.compile_hld;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import rv_fpga_plc_ide.helper.Data;
import rv_fpga_plc_ide.helper.Output_Tap;
import rv_fpga_plc_ide.helper.private_threads.compile_analysis_synthesis;
import rv_fpga_plc_ide.helper.private_threads.compile_assembler;
import rv_fpga_plc_ide.helper.private_threads.compile_fitter;
import rv_fpga_plc_ide.helper.private_threads.compile_update_mif;

/**
 *
 * @author hossameldin
 */
public class CompileHLD {
    
    public void compile_hdl(Component parentComponent, String Project_Folder, java.awt.event.ActionEvent evt, int hdl_compilation_type, JDialog jDialog_Loading, JFileChooser jFileChooser1) {
        Project_Folder = Project_Folder + "/q_files/";
        boolean no_hardware_change = check_hardware_change();
        if (((hdl_compilation_type == Data.SW_COMPILATION) || (hdl_compilation_type == Data.HW_COMPILATION && no_hardware_change)) &&
                Data.hdl_compilation_type == hdl_compilation_type) {
            switch (Data.hdl_compilation_state) {
                case Data.NO_COMPILATION:
                    compile_analysis_synthesis cas = new compile_analysis_synthesis(parentComponent, Project_Folder, evt, hdl_compilation_type, jDialog_Loading, jFileChooser1);
                    cas.start();
                    break;
                case Data.ANALYSIS_SYNTHESIS:
                    compile_fitter cf = new compile_fitter(parentComponent, Project_Folder, evt, hdl_compilation_type, jDialog_Loading, jFileChooser1);
                    cf.start();
                    break;
                case Data.FITTER:
                    compile_assembler ca = new compile_assembler(parentComponent, Project_Folder, evt, hdl_compilation_type, jDialog_Loading, jFileChooser1);
                    ca.start();
                    break;
                case Data.ASSEMBLER:
                    compile_update_mif cum = new compile_update_mif(parentComponent, Project_Folder, evt, hdl_compilation_type, jDialog_Loading, jFileChooser1);
                    cum.start();
                    break;
                case Data.UPDATED:
                    new Output_Tap().println("No Need for Compilling");
                    jDialog_Loading.hide();
                    JOptionPane.showMessageDialog(parentComponent, "No Need for Compilling");
            }
        } else {
            compile_analysis_synthesis cas = new compile_analysis_synthesis(parentComponent, Project_Folder, evt, hdl_compilation_type, jDialog_Loading, jFileChooser1);
            cas.start();
        }
    }
    
    private boolean check_hardware_change() {
        if (Data.Number_Of_Timers_Compiled != Data.Number_Of_Timers_In_Program) {
            return false;
        }
        if (Data.Number_Of_PWMs_Compiled != Data.Number_Of_PWMs_In_Program) {
            return false;
        }
        return true;
    }
}