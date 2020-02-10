/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rv_fpga_plc_ide.helper.RV64.compile_il;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import rv_fpga_plc_ide.helper.Data;
import rv_fpga_plc_ide.helper.GeneralFunctions;
import rv_fpga_plc_ide.helper.RV64.Write_Generated_Files.Write_Hardware_Files;
import rv_fpga_plc_ide.helper.RV64.Write_Generated_Files.Write_Software_Files;
import rv_fpga_plc_ide.helper.RV64.compile_c.compile_c_file;
import rv_fpga_plc_ide.helper.compile_hld.CompileHLD;
import rv_fpga_plc_ide.helper.private_threads.LoadingDialoge;

/**
 *
 * @author hossameldin
 */
public class Hardware {
    public void compile_hardware(Component parentComponent, String Project_Folder, ActionEvent evt, boolean compile_all_project, JLabel JTextLableLoading, JDialog jDialog_Loading, JFileChooser jFileChooser1, JTextArea jTextArea_Output_Tab) {
        Data.is_fpu_RV64_enabeled = false;
        Data.is_mul_RV64_enabeled = false;
        Data.is_div_RV64_enabeled = false;
        if (Data.hdl_compilation_state_RV64_HW == Data.UPDATED) {
            Data.hdl_compilation_state_RV64_HW = Data.ASSEMBLER;
        }
        LoadingDialoge loading = new LoadingDialoge("Compiling ...", JTextLableLoading, jDialog_Loading);
        loading.start();
        jTextArea_Output_Tab.setText("");
        jTextArea_Output_Tab.append("Start Compiling As Hardware.\n");
        boolean success = true;
        File c_files = new File(Project_Folder+"/c_files_RV64_HW");
        File q_files = new File(Project_Folder+"/q_files_RV64_HW");
        
        c_files.mkdirs();
        if (compile_all_project) q_files.mkdirs();
        
        jTextArea_Output_Tab.append("  Start Compiling \"instruction list\".\n");
        success &= compill_il_file_hw(parentComponent, jDialog_Loading);
        if (success) {
            jTextArea_Output_Tab.append("  Start Compiling \"hex2mifc\".\n");
            success &= new compile_c_file().compile_hex2mif(jTextArea_Output_Tab, Data.HW_COMPILATION);
        }
        if (success) {
            jTextArea_Output_Tab.append("  Start Compiling \"elf2rawx\".\n");
            success &= new compile_c_file().compile_elf2rawx(jTextArea_Output_Tab, Data.HW_COMPILATION);
        }
        if (success) {
            jTextArea_Output_Tab.append("  Start Compiling \"Application\".\n");
            success &= new compile_c_file().compile_application(jTextArea_Output_Tab, Data.HW_COMPILATION);
        }
        if (success) {
            jTextArea_Output_Tab.append("  Start Compiling \"Boot\".\n");
            success &= new compile_c_file().compile_boot(jTextArea_Output_Tab, Data.HW_COMPILATION);
        }
        if (success && compile_all_project) {
            jTextArea_Output_Tab.append("  Start Writting Hardware Files.\n");
            new Write_Hardware_Files().generate_q_files(Project_Folder+"/q_files_RV64_HW/", Data.HW_COMPILATION);
            jTextArea_Output_Tab.append("  Start Compiling \"Quartus Project\".\n");
            new GeneralFunctions().copy_file(Project_Folder+"/c_files_RV64_HW/"+Data.Project_Name+"_application/bin/"+Data.Project_Name+".mif", Project_Folder+"/q_files_RV64_HW/"+Data.Project_Name+".mif");
            new GeneralFunctions().copy_file(Project_Folder+"/c_files_RV64_HW/boot/bin/bootimage.mif", Project_Folder+"/q_files_RV64_HW/bootimage.mif");
            new CompileHLD().compile_hdl(parentComponent, Project_Folder, evt, Data.HW_COMPILATION, jDialog_Loading, jFileChooser1, jTextArea_Output_Tab);
        }
        
        if (success) {
            if (!compile_all_project) {
                jDialog_Loading.hide();
                JOptionPane.showMessageDialog(parentComponent, "Successful");
                jTextArea_Output_Tab.append("Compilling Finished Successfully\n");
            }
        } else {
            Icon icon = UIManager.getIcon("OptionPane.errorIcon");
            jDialog_Loading.hide();
            JOptionPane.showMessageDialog(parentComponent, "Not Successful", "Compile As Software", JOptionPane.OK_OPTION, icon);
            jTextArea_Output_Tab.append("Compilling did not Finished Successfully\n");
        }
    }
    
    private boolean compill_il_file_hw(Component parentComponent, JDialog jDialog_Loading) {
        boolean success = true;
        Data.Number_Of_Timers_In_Program = 0;
        Data.Number_Of_PWMs_In_Program = 0;
        Data.C_code =   "/*****************************************************************************\n" +
                        " * @file\n" +
                        " * @author   Sergey Khabarov\n" +
                        " * @editor   Hossameldin Eassa\n" +
                        " * @brief    Generated file for RV64 Application for PLC\n" +
                        " ****************************************************************************/\n" +
                        "\n" +
                        "/*****************************************************************************\n" +
                        " * Includes\n" +
                        " ****************************************************************************/\n" +
                        "\n" +
                        "#include <inttypes.h>\n" +
                        "#include <string.h>\n" +
                        "#include <stdio.h>\n" +
                        "#include \"axi_maps.h\"\n" +
                        "\n" +
                        "/*****************************************************************************\n" +
                        " * Static Functions\n" +
                        " ****************************************************************************/\n" +
                        "\n" +
                        "extern char _end;\n" +
                        "\n" +
                        "/**\n" +
                        " * @name sbrk\n" +
                        " * @brief Increase program data space.\n" +
                        " * @details Malloc and related functions depend on this.\n" +
                        " */\n" +
                        "char *sbrk(int incr) {\n" +
                        "    return &_end;\n" +
                        "}\n" +
                        "\n" +
                        "void print_uart_hex(long val) {\n" +
                        "    unsigned char t, s;\n" +
                        "    uart_map *uart = (uart_map *)ADDR_NASTI_SLAVE_UART1;\n" +
                        "    for (int i = 0; i < 16; i++) {\n" +
                        "        while (uart->status & UART_STATUS_TX_FULL) {}\n" +
                        "        \n" +
                        "        t = (unsigned char)((val >> ((15 - i) * 4)) & 0xf);\n" +
                        "        if (t < 10) {\n" +
                        "            s = t + '0';\n" +
                        "        } else {\n" +
                        "            s = (t - 10) + 'a';\n" +
                        "        }\n" +
                        "        uart->data = s;\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "void print_uart(const char *buf, int sz) {\n" +
                        "    uart_map *uart = (uart_map *)ADDR_NASTI_SLAVE_UART1;\n" +
                        "    for (int i = 0; i < sz; i++) {\n" +
                        "        while (uart->status & UART_STATUS_TX_FULL) {}\n" +
                        "        uart->data = buf[i];\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "/*****************************************************************************\n" +
                        " * Global Variables\n" +
                        " ****************************************************************************/\n\n";
        new Write_Software_Files().declareAndInitializeVariables();
        Data.C_code +=  "\nvoid "+Data.Project_Name+"() {\n\n" +
                        Data.localVariables +
                        "	io_per io_per_d;\n" +
                        "	time_measurement time_measurement_d;\n\n" +
                        Data.initializeLocalVariables +
                        "	io_per_d.registers = (volatile void *)ADDR_NASTI_SLAVE_GPIO;\n" +
                        "	time_measurement_d.registers = (volatile void *)ADDR_NASTI_SLAVE_MEASUREMENT;\n\n" +
                        "	while(1){\n" +
                        "\n" +
                        "		start_time(&time_measurement_d);\n" +
                        "		io_per_set_output(&io_per_d, RWD, 0, 0);\n";
        
        Data.Load_index = 0;
        for (int rung_i = 0; rung_i < Data.size_Rung; rung_i++) {
            Data.C_code += "\n\t\t// Rung " + (rung_i + 1 ) + " :" + Data.Rung_Name[rung_i].replaceAll(":", "") + "\n";
            success &= compile_rung_hw(parentComponent, rung_i, jDialog_Loading);
        }
        
        Data.C_code += "        stop_time(&time_measurement_d);\n" +
                       "    }\n" +
                       "\n" +
                       "}";
        new Write_Software_Files().write_library_files(Data.Project_Folder.getPath()+"/c_files_RV64_HW", Data.HW_COMPILATION);
        new GeneralFunctions().write_file(Data.Project_Folder.getPath()+"/c_files_RV64_HW/"+Data.Project_Name+"_application/src/"+Data.Project_Name+".c", Data.C_code);
        return success;
    }
    
    private boolean compile_rung_hw(Component parentComponent, int rung_i, JDialog jDialog_Loading) {
        boolean success = true;
        String il_inst;
        for (int program_i = 0; program_i < Data.size_Program_in_rung[rung_i]; program_i++) {
            il_inst = Data.Program_2D[rung_i][program_i];
            String[] il_inst_Arr = new String[1];
            il_inst_Arr[0] = il_inst;
            new GeneralFunctions().remove_Spaces_Before_Strings(il_inst_Arr, 1);
            il_inst = il_inst_Arr[0];
            if (il_inst.split(" ")[0].contains("LDN")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("LDN", "");
                success = success && add_basic_load_command(parentComponent, Operand, "~");
            } else if (il_inst.split(" ")[0].contains("STN")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("STN", "");
                success = success && add_basic_store_command(parentComponent, Operand, "~");
            } else if (il_inst.split(" ")[0].contains("SET")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("SET", "");
                add_set_reset_c_command(parentComponent, Operand, 1);
            } else if (il_inst.split(" ")[0].contains("RST")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("RST", "");
                add_set_reset_c_command(parentComponent, Operand, 0);
            } else if (il_inst.split(" ")[0].contains("LD")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("LD", "");
                success = success && add_basic_load_command(parentComponent, Operand, "");
            } else if (il_inst.split(" ")[0].contains("ST")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("ST", "");
                success = success && add_basic_store_command(parentComponent, Operand, "");
            } else if (il_inst.split(" ")[0].contains("ANDN")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("ANDN", "");
                add_basic_c_command(Operand, "&", "~");
            } else if (il_inst.split(" ")[0].contains("XORN")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("XOR", "");
                add_basic_c_command(Operand, "^", "~");
            } else if (il_inst.split(" ")[0].contains("ORN")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("OR", "");
                add_basic_c_command(Operand, "|", "~");
            } else if (il_inst.split(" ")[0].contains("ANDB")) {
                Data.C_code += "\t\tvar"+(Data.Load_index - 1)+" &= var"+(Data.Load_index - 2)+";\n";
            } else if (il_inst.split(" ")[0].contains("XORB")) {
                Data.C_code += "\t\tvar"+(Data.Load_index - 1)+" ^= var"+(Data.Load_index - 2)+";\n";
            } else if (il_inst.split(" ")[0].contains("ORB")) {
                Data.C_code += "\t\tvar"+(Data.Load_index - 1)+" |= var"+(Data.Load_index - 2)+";\n";
            } else if (il_inst.split(" ")[0].contains("AND")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("AND", "");
                add_basic_c_command(Operand, "&", "");
            } else if (il_inst.split(" ")[0].contains("XOR")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("XOR", "");
                add_basic_c_command(Operand, "^", "");
            } else if (il_inst.split(" ")[0].contains("OR")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("OR", "");
                add_basic_c_command(Operand, "|", "");
            } else if (il_inst.split(" ")[0].contains("ADD")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("ADD", "");
                add_basic_c_command(Operand, "+", "");
            } else if (il_inst.split(" ")[0].contains("SUB")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("SUB", "");
                add_basic_c_command(Operand, "-", "");
            } else if (il_inst.split(" ")[0].contains("MUL")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("MUL", "");
                add_basic_c_command(Operand, "*", "");
                Data.is_mul_RV64_enabeled = true;
            } else if (il_inst.split(" ")[0].contains("DIV")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("DIV", "");
                add_basic_c_command(Operand, "/", "");
                Data.is_div_RV64_enabeled = true;
            } else if (il_inst.split(" ")[0].contains("MOD")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("MOD", "");
                add_basic_c_command(Operand, "%", "");
                Data.is_div_RV64_enabeled = true;
            } else if (il_inst.split(" ")[0].contains("NOT")) {
                Data.C_code += "\t\tvar"+(Data.Load_index - 1)+" = ~var"+(Data.Load_index - 1)+";\n";
            } else if (il_inst.split(" ")[0].contains("GT")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("GT", "");
                add_comparison_c_command(Operand, ">");
            } else if (il_inst.split(" ")[0].contains("GE")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("GE", "");
                add_comparison_c_command(Operand, ">=");
            } else if (il_inst.split(" ")[0].contains("EQ")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("EQ", "");
                add_comparison_c_command(Operand, "==");
            } else if (il_inst.split(" ")[0].contains("NE")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("NE", "");
                add_comparison_c_command(Operand, "!=");
            } else if (il_inst.split(" ")[0].contains("LT")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("LT", "");
                add_comparison_c_command(Operand, "<");
            } else if (il_inst.split(" ")[0].contains("LE")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("LE", "");
                add_comparison_c_command(Operand, "<=");
            } else if (il_inst.split(" ")[0].contains("CAL")) {
                String Operand = il_inst.replaceAll(" ", "").replaceAll("CAL", "").replaceAll("\\(", "");
                String Function_Block_Name = Operand;
                
                program_i++; // first operand
                il_inst = Data.Program_2D[rung_i][program_i];
                il_inst_Arr[0] = il_inst;
                new GeneralFunctions().remove_Spaces_Before_Strings(il_inst_Arr, 1);
                il_inst = il_inst_Arr[0].replaceAll(" ", "");
                il_inst = il_inst.replaceAll(",", "");
                
                String typeOfVariable = "No Type";
                for (int i = 1; i < Data.size_Vaiables-1; i++) {
                    String Variable_temp = Data.Vaiables[i].replace(" ", "");
                    if (Variable_temp.contains(Operand)) {
                        typeOfVariable = Variable_temp.split(":")[1];
                        break;
                    }
                }
                if (typeOfVariable.contains("TON")) {
                    success = TON_compile_hw(parentComponent, Function_Block_Name, il_inst, rung_i, program_i);
                    program_i = program_i + 3;
                } else if (typeOfVariable.contains("PWM")) {
                    success = PWM_compile_hw(parentComponent, Function_Block_Name, il_inst, rung_i, program_i);
                    program_i = program_i + 2;
                } else {
                    JOptionPane.showMessageDialog(parentComponent, "\""+typeOfVariable+"\"not supported yet", "Compile il", JOptionPane.OK_OPTION);
                    success = false;
                }
            } else {
                JOptionPane.showMessageDialog(parentComponent, "\""+il_inst+"\"not supported yet", "Compile il", JOptionPane.OK_OPTION);
                success = false;
            }
        }
        return success;
    }
    
    private boolean add_basic_load_command(Component parentComponent, String Operand, String not) {
        boolean success = true;
        int Instant_Operand;
        if (Operand.contains("%")){
            Operand = Operand.replaceAll("%", "");
            String offc = Operand.split("\\.")[1];
            Operand = Operand.split("\\.")[0];
            Data.C_code += "\t\tuint32_t var"+Data.Load_index+" = "+not+"io_per_get_input(&io_per_d, "+Operand+", "+offc+");\n";
        } else if (Operand.contains("T#")) {
            if (not.equals("")) {
                double time_sec = new GeneralFunctions().getSecFromTimeFormat(Operand);
                long Number_of_Clocks = (long) (time_sec*(double)Data.CPU_RV64_Timer_Freq);
                Data.C_code += "\t\tuint64_t var"+Data.Load_index+" = (uint64_t)"+Number_of_Clocks+";\n";
            } else {
                Icon icon = UIManager.getIcon("OptionPane.errorIcon");
                JOptionPane.showMessageDialog(parentComponent, "Time can't be invertes!", "Compile As Software", JOptionPane.OK_OPTION, icon);
                success = false;
            }
        } else {
            try {
                Instant_Operand = Integer.parseInt(Operand);
                Data.C_code += "\t\tuint32_t var"+Data.Load_index+" = "+not+Instant_Operand+";\n";
            } catch (NumberFormatException ex) {
                String Variable_temp;
                String typeOfVariable = "Variabe Not Found";
                String nameOfVariable = "Variabe Not Found";
                String Type = "NotSupported";
                for (int i = 1; i < Data.size_Vaiables-1; i++) {
                    Variable_temp = Data.Vaiables[i].replace(" ", "");
                    if (Variable_temp.contains(Operand)) {
                        nameOfVariable = Variable_temp.split(":")[0];
                        typeOfVariable = Variable_temp.split(":")[1];
                        break;
                    }
                }
                switch (typeOfVariable) {
                    case "INT":
                        Type = "uint32_t";
                        break;
                    case "BOOL":
                        Type = "uint32_t";
                        break;
                    case "REAL":
                        Type = "float";
                        break;
                    case "TIME":
                        Type = "uint64_t";
                        break;
                    default:
                        break;
                }
                Data.C_code += "\t\t"+Type+" var"+Data.Load_index+" = "+not+nameOfVariable+";\n";
            }
        }
        Data.Load_index++;
        return success;
    }
    
    private boolean add_basic_store_command(Component parentComponent, String Operand, String not) {
        boolean success = true;
        if (Operand.contains("%")){
            Operand = Operand.replaceAll("%", "");
            String offc = Operand.split("\\.")[1];
            Operand = Operand.split("\\.")[0];
            Data.C_code += "\t\tio_per_set_output(&io_per_d, "+Operand+", "+offc+", "+not+"var"+(Data.Load_index-1)+");\n";
        } else {
            try {
                Integer.parseInt(Operand);
                Icon icon = UIManager.getIcon("OptionPane.errorIcon");
                JOptionPane.showMessageDialog(parentComponent, "Can not add instanse in store command", "Compile As Software", JOptionPane.OK_OPTION, icon);
                success = false;
            } catch (NumberFormatException ex) {
                String Variable_temp;
                String nameOfVariable = "Variabe Not Found";
                for (int i = 1; i < Data.size_Vaiables-1; i++) {
                    Variable_temp = Data.Vaiables[i].replace(" ", "");
                    if (Variable_temp.contains(Operand)) {
                        nameOfVariable = Variable_temp.split(":")[0];
                        break;
                    }
                }
                Data.C_code += "\t\t"+nameOfVariable+" = "+not+"var"+(Data.Load_index-1)+";\n";
            }
        }
        return success;
    }
    
    private boolean add_set_reset_c_command(Component parentComponent, String Operand, int set_reset) {
        boolean success = true;
        if (Operand.contains("%")){
            Operand = Operand.replaceAll("%", "");
            String offc = Operand.split("\\.")[1];
            Operand = Operand.split("\\.")[0];
            Data.C_code += "\t\tif (var"+(Data.Load_index - 1)+" != 0) io_per_set_output(&io_per_d, "+Operand+", "+offc+", "+set_reset+");\n";
        } else {
            try {
                Integer.parseInt(Operand);
                Icon icon = UIManager.getIcon("OptionPane.errorIcon");
                JOptionPane.showMessageDialog(parentComponent, "Can not add instanse in store command", "Compile As Software", JOptionPane.OK_OPTION, icon);
                success = false;
            } catch (NumberFormatException ex) {
                String Variable_temp;
                String nameOfVariable = "Variabe Not Found";
                for (int i = 1; i < Data.size_Vaiables-1; i++) {
                    Variable_temp = Data.Vaiables[i].replace(" ", "");
                    if (Variable_temp.contains(Operand)) {
                        nameOfVariable = Variable_temp.split(":")[0];
                        break;
                    }
                }
                Data.C_code += "\t\tif (var"+(Data.Load_index - 1)+" != 0) "+nameOfVariable+" = "+set_reset+";\n";
            }
        }
        return success;
    }
    
    private void add_basic_c_command(String Operand, String operation, String not) {
        int Instant_Operand;
        if (Operand.contains("%")){
            Operand = Operand.replaceAll("%", "");
            String offc = Operand.split("\\.")[1];
            Operand = Operand.split("\\.")[0];
            Data.C_code += "\t\tvar"+(Data.Load_index - 1)+" "+operation+"= "+not+"io_per_get_input(&io_per_d, "+Operand+", "+offc+");\n";
        } else {
            try {
                Instant_Operand = Integer.parseInt(Operand);
                Data.C_code += "\t\tvar"+(Data.Load_index - 1)+" "+operation+"= "+not+Instant_Operand+";\n";
            } catch (NumberFormatException ex) {
                String Variable_temp;
                String typeOfVariable = "Variabe Not Found";
                String nameOfVariable = "Variabe Not Found";
                String Type = "NotSupported";
                for (int i = 1; i < Data.size_Vaiables-1; i++) {
                    Variable_temp = Data.Vaiables[i].replace(" ", "");
                    if (Variable_temp.contains(Operand)) {
                        nameOfVariable = Variable_temp.split(":")[0];
                        typeOfVariable = Variable_temp.split(":")[1];
                        break;
                    }
                }
                switch (typeOfVariable) {
                    case "INT":
                        Type = "uint32_t";
                        break;
                    case "BOOL":
                        Type = "uint32_t";
                        break;
                    case "REAL":
                        Type = "float";
                        break;
                    case "TIME":
                        Type = "uint64_t";
                        break;
                    default:
                        break;
                }
                Data.C_code += "\t\tvar"+(Data.Load_index - 1)+" "+operation+"= "+not+nameOfVariable+";\n";
            }
        }
    }
    
    private void add_comparison_c_command(String Operand, String compare) {
        int Instant_Operand;
        if (Operand.contains("%")){
            Operand = Operand.replaceAll("%", "");
            String offc = Operand.split("\\.")[1];
            Operand = Operand.split("\\.")[0];
            Data.C_code += "\t\tif (var"+(Data.Load_index - 1)+" "+compare+" io_per_get_input(&io_per_d, "+Operand+", "+offc+")) var"+(Data.Load_index - 1)+" = 1; else var"+(Data.Load_index - 1)+" = 0;\n";
            } else if (Operand.contains("T#")) {
                double time_sec = new GeneralFunctions().getSecFromTimeFormat(Operand);
                long Number_of_Clocks = (long) (time_sec*(double)Data.CPU_RV64_Timer_Freq);
                Data.C_code += "\t\tif (var"+(Data.Load_index - 1)+" "+compare+" "+Number_of_Clocks+") var"+(Data.Load_index - 1)+" = 1; else var"+(Data.Load_index - 1)+" = 0;\n";
            } else {
            try {
                Instant_Operand = Integer.parseInt(Operand);
                Data.C_code += "\t\tif (var"+(Data.Load_index - 1)+" "+compare+" "+Instant_Operand+") var"+(Data.Load_index - 1)+" = 1; else var"+(Data.Load_index - 1)+" = 0;\n";
            } catch (NumberFormatException ex) {
                String Variable_temp;
                String nameOfVariable = "Variabe Not Found";
                for (int i = 1; i < Data.size_Vaiables-1; i++) {
                    Variable_temp = Data.Vaiables[i].replace(" ", "");
                    if (Variable_temp.contains(Operand)) {
                        nameOfVariable = Variable_temp.split(":")[0];
                        break;
                    }
                }
                
                Data.C_code += "\t\tif (var"+(Data.Load_index - 1)+" "+compare+" "+nameOfVariable+") var"+(Data.Load_index - 1)+" = 1; else var"+(Data.Load_index - 1)+" = 0;\n";
            }
        }
    }
    
    private boolean TON_compile_hw(Component parentComponent, String Timer_Name, String il_inst, int rung_i, int program_i){
        String typeOfVariable;
        String[] il_inst_Arr = new String[1];
        
        Data.Name_of_Timers[Data.Number_Of_Timers_In_Program] = Timer_Name;
        Data.Number_Of_Timers_In_Program++;
                
        Data.C_code = new GeneralFunctions().insertStringAfter(Data.localVariables, "\ttimer_hw "+Timer_Name+";\n", Data.C_code);
        Data.C_code = new GeneralFunctions().insertStringAfter(Data.initializeLocalVariables, "\t"+Timer_Name+".registers = (volatile void *)ADDR_NASTI_SLAVE_TON_"+Timer_Name+";\n", Data.C_code);
                
        Data.C_code += "\n\t\t// TON "+Timer_Name+"\n";
        String Operand = il_inst.split(":=")[1];
        int Instant_Operand;
        if (Operand.contains("%")){
            Operand = Operand.replaceAll("%", "");
            String offc = Operand.split("\\.")[1];
            Operand = Operand.split("\\.")[0];
            Data.C_code += "\t\tint var"+Data.Load_index+" = io_per_get_input(&io_per_d, "+Operand+", "+offc+");\n";
        } else {
            try {
                Instant_Operand = Integer.parseInt(Operand);
                Data.C_code += "\t\tint var"+Data.Load_index+" = "+Instant_Operand+";\n";
            } catch (NumberFormatException ex) {
                String Variable_temp;
                typeOfVariable = "Not Supported Type";
                String nameOfVariable = "Variabe Not Found";
                for (int i = 1; i < Data.size_Vaiables-1; i++) {
                    Variable_temp = Data.Vaiables[i].replace(" ", "");
                    if (Variable_temp.contains(Operand)) {
                        nameOfVariable = Variable_temp.split(":")[0];
                        typeOfVariable = Variable_temp.split(":")[1];
                        break;
                    }
                }
                if (typeOfVariable.equals("BOOL") || typeOfVariable.equals("INT")) {
                    Data.C_code += "\t\tint var"+Data.Load_index+" = "+nameOfVariable+";\n";
                } else {
                    JOptionPane.showMessageDialog(parentComponent, "Type of Variable\""+nameOfVariable+"\" should be \"BOOL\" or \"INT\".", "Compile il HW", JOptionPane.OK_OPTION);
                    return false;
                }
            }
        }
                 
        program_i++; // Preset Time (PT)
        il_inst = Data.Program_2D[rung_i][program_i];
        il_inst_Arr[0] = il_inst;
        new GeneralFunctions().remove_Spaces_Before_Strings(il_inst_Arr, 1);
        il_inst = il_inst_Arr[0].replaceAll(" ", "");
        il_inst = il_inst.replaceAll(",", "");
        Operand = il_inst.split(":=")[1];
                   
        typeOfVariable = "No Type";
        for (int i = 1; i < Data.size_Vaiables-1; i++) {
            String Variable_temp = Data.Vaiables[i].replace(" ", "");
            if (Variable_temp.contains(Operand)) {
                typeOfVariable = Variable_temp.split(":")[1];
                break;
            }
        }
                   
        String Preset_Time;
                 
        if (typeOfVariable.contains("TIME")) {
            Preset_Time = Operand;
        } else if (Operand.contains("T#")) {
            double time_sec = new GeneralFunctions().getSecFromTimeFormat(Operand);
            long Number_of_Clocks = (long) (time_sec*(double)Data.CPU_RV64_Timer_Freq);
            Preset_Time = "(uint64_t)"+Number_of_Clocks;
        } else {
            JOptionPane.showMessageDialog(parentComponent, "Preset time should be variable with type \"TIME\" or instant begins with T#.", "Compile il HW", JOptionPane.OK_OPTION);
            return false;
        }
                    
        program_i++; // Elapsed Time (ET)
        il_inst = Data.Program_2D[rung_i][program_i];
        il_inst_Arr[0] = il_inst;
        new GeneralFunctions().remove_Spaces_Before_Strings(il_inst_Arr, 1);
        il_inst = il_inst_Arr[0].replaceAll(" ", "");
        il_inst = il_inst.replaceAll(",", "");
        Operand = il_inst.split("=>")[1];
                    
        String Elapsed_Time;
                   
        typeOfVariable = "No Type";
        for (int i = 1; i < Data.size_Vaiables-1; i++) {
            String Variable_temp = Data.Vaiables[i].replace(" ", "");
            if (Variable_temp.contains(Operand)) {
                typeOfVariable = Variable_temp.split(":")[1];
                break;
            }
        }
                   
        if (typeOfVariable.contains("TIME")) {
            Elapsed_Time = Operand;
        } else {
            JOptionPane.showMessageDialog(parentComponent, "Elapsed time should be variable with type \"TIME\".", "Compile il HW", JOptionPane.OK_OPTION);
            return false;
        }
                    
        program_i++; // Q
        il_inst = Data.Program_2D[rung_i][program_i];
        il_inst_Arr[0] = il_inst;
        new GeneralFunctions().remove_Spaces_Before_Strings(il_inst_Arr, 1);
        il_inst = il_inst_Arr[0].replaceAll(" ", "");
        il_inst = il_inst.replaceAll("\\)", "");
        Operand = il_inst.split("=>")[1];
                    
        String Output_Timer;
        if (Operand.contains("%")){
            Operand = Operand.replaceAll("%", "");
            String offc = Operand.split("\\.")[1];
            Operand = Operand.split("\\.")[0];
            Output_Timer = "\t\tio_per_set_output(&io_per_d, "+Operand+", "+offc+", timer_hw_recieve_Q(&"+Timer_Name+"));\n";
        } else {
            try {
                int Integer_Operand = Integer.parseInt(Operand);
                JOptionPane.showMessageDialog(parentComponent, "Tho output \""+Integer_Operand+"\" of the timer shouldn't be instant value.", "Compile il HW", JOptionPane.OK_OPTION);
                return false;
            } catch (NumberFormatException ex) {
                String Variable_temp;
                typeOfVariable = "Not Supported Type";
                String nameOfVariable = "Variabe Not Found";
                for (int i = 1; i < Data.size_Vaiables-1; i++) {
                    Variable_temp = Data.Vaiables[i].replace(" ", "");
                    if (Variable_temp.contains(Operand)) {
                        nameOfVariable = Variable_temp.split(":")[0];
                        typeOfVariable = Variable_temp.split(":")[1];
                        break;
                    }
                }
                            
                if (typeOfVariable.equals("BOOL") || typeOfVariable.equals("INT")) {
                    Output_Timer = "\t\t"+nameOfVariable+" = timer_hw_recieve_Q(&"+Timer_Name+");\n";
                } else {
                    JOptionPane.showMessageDialog(parentComponent, "Type of Variable\""+nameOfVariable+"\" should be \"BOOL\" or \"INT\".", "Compile il HW", JOptionPane.OK_OPTION);
                    return false;
                }
            }
        }
                   
        Data.C_code +="\t\ttimer_hw_send_preset_time(&"+Timer_Name+", "+Preset_Time+");\n"
                    + "\t\ttimer_hw_send_in(&"+Timer_Name+", var"+Data.Load_index+");\n"
                    + "\t\t"+Elapsed_Time+" = timer_hw_recieve_elapsed_time(&"+Timer_Name+");\n"
                    + Output_Timer
                    + "\n";
                    
        Data.Load_index++;
        return true;
    }
    
    private boolean PWM_compile_hw(Component parentComponent, String PWM_Name, String il_inst, int rung_i, int program_i) {
        String typeOfVariable;
        String[] il_inst_Arr = new String[1];
        String Frequency = "Error Freq";
                
        Data.Name_of_PWMs[Data.Number_Of_PWMs_In_Program] = PWM_Name;
        Data.Number_Of_PWMs_In_Program++;
                       
        Data.C_code = new GeneralFunctions().insertStringAfter(Data.localVariables, "\tpwm_hw "+PWM_Name+";\n", Data.C_code);
        Data.C_code = new GeneralFunctions().insertStringAfter(Data.initializeLocalVariables, "\t"+PWM_Name+".registers = (volatile void *)ADDR_NASTI_SLAVE_PWM_"+PWM_Name+";\n", Data.C_code);
        
        Data.C_code += "\n\t\t// PWM "+PWM_Name+"\n";
        String Operand = il_inst.split(":=")[1];
        
        if (Operand.contains("%")){
            JOptionPane.showMessageDialog(parentComponent, "Frequency can't be BOOL.", "Compile il HW", JOptionPane.OK_OPTION);
            return false;
        } else {
            try {
                Integer.parseInt(Operand);
                Frequency = Operand;
            } catch (NumberFormatException ex) {
                String Variable_temp;
                typeOfVariable = "Not Supported Type";
                String nameOfVariable = "Variabe Not Found";
                for (int i = 1; i < Data.size_Vaiables-1; i++) {
                    Variable_temp = Data.Vaiables[i].replace(" ", "");
                    if (Variable_temp.contains(Operand)) {
                        nameOfVariable = Variable_temp.split(":")[0];
                        typeOfVariable = Variable_temp.split(":")[1];
                        break;
                    }
                }
                if (typeOfVariable.equals("INT")) {
                    Frequency = nameOfVariable;
                } else {
                    JOptionPane.showMessageDialog(parentComponent, "Type of Variable\""+nameOfVariable+"\" should be \"INT\".", "Compile il HW", JOptionPane.OK_OPTION);
                    return false;
                }
            }
        }
                       
        program_i++; // Duty Cycle (DC)
        il_inst = Data.Program_2D[rung_i][program_i];
        il_inst_Arr[0] = il_inst;
        new GeneralFunctions().remove_Spaces_Before_Strings(il_inst_Arr, 1);
        il_inst = il_inst_Arr[0].replaceAll(" ", "");
        il_inst = il_inst.replaceAll(",", "");
        Operand = il_inst.split(":=")[1];
                        
        typeOfVariable = "No Type";
        for (int i = 1; i < Data.size_Vaiables-1; i++) {
            String Variable_temp = Data.Vaiables[i].replace(" ", "");
            if (Variable_temp.contains(Operand)) {
                typeOfVariable = Variable_temp.split(":")[1];
                break;
            }
        }
                        
        String Duty_Cycle;
                     
        if (Operand.contains("%")){
            JOptionPane.showMessageDialog(parentComponent, "Duty Cycle can't be BOOL.", "Compile il HW", JOptionPane.OK_OPTION);
            return false;
        } else if (Operand.contains("T#")) {
            JOptionPane.showMessageDialog(parentComponent, "Duty Cycle can't be TIME.", "Compile il HW", JOptionPane.OK_OPTION);
            return false;
        } else {
            try {
                Integer.parseInt(Operand);
                Duty_Cycle = Operand;
            } catch (NumberFormatException ex) {
                if (typeOfVariable.equals("INT")) {
                    Duty_Cycle = Operand;
                } else {
                    JOptionPane.showMessageDialog(parentComponent, "Duty cycle should be with type \"INT\".", "Compile il HW", JOptionPane.OK_OPTION);
                    return false;
                }
            }
        }
                        
        program_i++; // Q
        il_inst = Data.Program_2D[rung_i][program_i];
        il_inst_Arr[0] = il_inst;
        new GeneralFunctions().remove_Spaces_Before_Strings(il_inst_Arr, 1);
        il_inst = il_inst_Arr[0].replaceAll(" ", "");
        il_inst = il_inst.replaceAll("\\)", "");
        Operand = il_inst.split("=>")[1];
                      
        String Output_Timer;
        if (Operand.contains("%")){
            Operand = Operand.replaceAll("%", "");
            String offc = Operand.split("\\.")[1];
            Operand = Operand.split("\\.")[0];
            Output_Timer = "\t\tio_per_set_output(&io_per_d, "+Operand+", "+offc+", pwm_hw_recieve_Q(&"+PWM_Name+"));\n";
        } else {
            try {
                int Integer_Operand = Integer.parseInt(Operand);
                JOptionPane.showMessageDialog(parentComponent, "Tho output of the timer \""+Integer_Operand+"\" shouldn't be instant value.", "Compile il HW", JOptionPane.OK_OPTION);
                return false;
            } catch (NumberFormatException ex) {
                String Variable_temp;
                typeOfVariable = "Not Supported Type";
                String nameOfVariable = "Variabe Not Found";
                for (int i = 1; i < Data.size_Vaiables-1; i++) {
                    Variable_temp = Data.Vaiables[i].replace(" ", "");
                    if (Variable_temp.contains(Operand)) {
                        nameOfVariable = Variable_temp.split(":")[0];
                        typeOfVariable = Variable_temp.split(":")[1];
                        break;
                    }
                }
                            
                if (typeOfVariable.equals("BOOL") || typeOfVariable.equals("INT")) {
                    Output_Timer = "\t\t"+nameOfVariable+" = pwm_hw_recieve_Q(&"+PWM_Name+");\n";
                } else {
                    JOptionPane.showMessageDialog(parentComponent, "Type of Variable\""+nameOfVariable+"\" should be \"BOOL\" or \"INT\".", "Compile il HW", JOptionPane.OK_OPTION);
                    return false;
                }
            }
        }
                       
        Data.C_code +="\t\tpwm_hw_send_frequency_duty_cycle(&"+PWM_Name+", "+Frequency+", "+Duty_Cycle+");\n"
                    + Output_Timer
                    + "\n"
                    + "";
                      
        Data.Load_index++;
        return true;
    }
}
