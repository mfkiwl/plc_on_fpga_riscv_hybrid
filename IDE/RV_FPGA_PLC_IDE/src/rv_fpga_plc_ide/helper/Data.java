/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rv_fpga_plc_ide.helper;

import java.io.File;
import java.util.Date;
import javax.swing.JTextField;

/**
 *
 * @author hossa
 */
public class Data {
    public static int size_Program = 0;
    public static int[] size_Program_in_rung;
    public static int max_size_program_in_rung;
    public static int size_Vaiables = 0;
    public static int size_Rung = 0;
    
    public static boolean is_New_Project;
    public static boolean is_Saved_Project;
    public static boolean is_There_is_a_project = false;
    public static boolean Commands_Enabeled = false;
    public static boolean Compile_Enabeled = false;
    
    public static String Project_Name = "No Project";
    public static String Project_Name_temp = "No Project";
    public static String[][] Program_2D;
    public static String[] Program_1D;
    public static String[] Vaiables;
    public static String[] Rung_Name;
    
    public static int Operator_Select = -1;
    public static String Basic_Comman_IL;
    public static int Selected_Rung;
    
    public static JTextField general_JTextField;
    public static boolean is_set;
    public static boolean is_TON;
    public static boolean is_bistable;
    public static boolean is_timer;
    public static boolean is_PWM;
    public static boolean is_PID;
    public static boolean is_newVariable_timer;
    public static boolean is_newVariable_PWM;
    public static boolean is_newVariable_PID;
    public static String Function_Name;
    
    public static File Project_Folder;
    
    public static int Max_Number_Of_Instructions = 256;
    
    public static final int MAX_NUMBER_OF_TIMERS_SW = 2;
    
    public static final String[] SUPPORTED_DATATYPES = new String[] 
    { 
        "BOOL", 
        "SINT", "INT", "DINT", "LINT", "USINT", "UINT", "UDINT", "ULINT", 
        "LREAL", 
        "TIME", 
        "TON", "TOF", "PWM", "PID"
    };
    public static final String[] SUPPORTED_TYPE_CONVERSION = new String[]
    {
        "BOOL", 
        "SINT", "INT", "DINT", "LINT", "USINT", "UINT", "UDINT", "ULINT", 
        "LREAL", 
        "TIME"
    };
    public static final String[] SUPPORTED_PWM_FRQ_DC = new String[]
    {
        "SINT", "INT", "DINT", "USINT", "UINT", "UDINT"
    };
    public static final String[] SUPPORTED_PID_VARIABLES = new String[]
    {
        "SINT", "INT", "DINT", "USINT", "UINT", "UDINT", 
        "LREAL"
    };
    
    public static final int NO_COMPILING = 0;
    public static final int SW_COMPILING = 1;
    public static final int HW_COMPILING = 2;
    public static int Compiling_Type = NO_COMPILING;
    
    // Compill il to c
    public static String C_code;
    public static int Load_index = 0;
    public static Boolean[] Load_index_is_defined;
    public static String[][] Load_index_Save;   // 0 >> Save compare or operation       (compare  or operation   )
                                                // 1 >> Determine compare or arethmetic (C        or not         )
                                                // 2 >> Determine suport boolean or not (true     or Support_BOOL)
                                                // 3 >> Determine type mismatch or no   (mismatch or false       )
    public static final int SUB_INDEX = 4;
    public static final int MAX_LOAD_INDEX = 1000;
    public static final int F_100M = 100000000;
    public static final int F_75M  = 75000000;
    public static final int F_70M  = 70000000;
    public static final int F_65M  = 65000000;
    public static final int F_50M  = 50000000;
    public static final int F_10M  = 10000000;
    public static final int F_1M   = 1000000;
    
    public static int       CPU_RV32_Freq = F_100M;
    public static int       CPU_RV32_Timer_Freq = F_100M;
    
    public static int       CPU_RV64_Freq = F_65M;
    public static int       CPU_RV64_Timer_Freq = F_65M;
    public static int       PWM_RV64_HW_Freq = F_1M;
    
    public static boolean RequestDownload = false;
    
    
    // Comile hdl
    public static final int NO_COMPILATION = 0;
    public static final int ANALYSIS_SYNTHESIS = 1;
    public static final int FITTER = 2;
    public static final int ASSEMBLER = 3;
    public static final int UPDATED = 4;
    public static int hdl_compilation_state_RV32_SW = NO_COMPILATION;
    public static int hdl_compilation_state_RV32_HW = NO_COMPILATION;
    public static int hdl_compilation_state_RV64_SW = NO_COMPILATION;
    public static int hdl_compilation_state_RV64_HW = NO_COMPILATION;
    
    public static final int MAX_FB_NUMBER = 50;
    public static int Number_Of_Timers_Compiled = 0;
    public static int Number_Of_Timers_In_Program = 0;
    public static int Number_Of_Timers_In_Program_SW = 0;
    public static String[] Name_of_Timers = new String[MAX_FB_NUMBER];
    
    public static int Number_Of_PWMs_Compiled = 0;
    public static int Number_Of_PWMs_In_Program = 0;
    public static String[] Name_of_PWMs = new String[MAX_FB_NUMBER];
    
    public static int Number_Of_PIDs_Compiled = 0;
    public static int Number_Of_PIDs_In_Program = 0;
    public static String[] Name_of_PIDs = new String[MAX_FB_NUMBER];
    
    public static int ALU_Support_Compiled_RV64_SW = 0;
    public static int ALU_Support_Compiled_RV64_HW = 0;
    public static int ALU_Support_In_Program_RV64_SW = 0;
    public static int ALU_Support_In_Program_RV64_HW = 0;
    public static boolean is_fpu_RV64_enabeled = false;
    public static boolean is_mul_RV64_enabeled = false;
    public static boolean is_div_RV64_enabeled = false;
    public static final int MASK_FPU_RV64 = 1;
    public static final int MASK_MUL_RV64 = 2;
    public static final int MASK_DIV_RV64 = 4;
            
    public static final int RV32 = 0;
    public static final int RV64 = 1;
    public static final int NO_CORE = 2;
    public static int core = NO_CORE;
    public static int compiled_core = NO_CORE;
    
    
    // Type of output from executing commands
    public static final int COMMAND_WINDOW = 0;
    public static final int OUTPUT_TAP_WINDOW = 1;
    public static final int COMMAND_OUTPUT_TAP_WINDOW = 2;
    public static final int NO_WINDOW = 3;
    public static int deafult_out_window = COMMAND_WINDOW;
    
    
    // Path to compilers
    public static final String R32_COMPILER_PATH = "/opt/riscv32/bin/";
    public static final String R64_COMPILER_PATH = "/opt/riscv64/bin/";
    
    public static final String localVariables           = "	/*************** Local Variables ****************/\n\n";
    public static final String initializeLocalVariables = "	/********** Initialize Local Variables **********/\n\n";
    public static final String instructionListVariables = "	/********** Instruction List Variables **********/\n\n";
    
    
    // Compiling time calculation
    public static Date StartTime;
    public static Date EndTime;
    
    // Supported Data types in compilation
    public static final int NO_TYPE  = 0;
    public static final int BOOL     = 1;
    public static final int UINT8_T  = 2;
    public static final int UINT16_T = 3;
    public static final int UINT32_T = 4;
    public static final int UINT64_T = 5;
    public static final int INT8_T   = 6;
    public static final int INT16_T  = 7;
    public static final int INT32_T  = 8;
    public static final int INT64_T  = 9;
    public static final int FLOAT    = 10;
    public static final int DOUBLE   = 11;
    public static final int TIME     = 12;
    public static final int TIMER    = 13;
    public static final int NAN      = 14;
    
    public static int[] Current_Register_Type = new int[MAX_LOAD_INDEX];
    public static int Current_Register_Count = 0;
    
}
