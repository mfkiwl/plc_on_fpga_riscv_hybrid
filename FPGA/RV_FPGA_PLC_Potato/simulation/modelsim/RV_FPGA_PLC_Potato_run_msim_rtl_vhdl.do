transcript on
if ![file isdirectory RV_FPGA_PLC_Potato_iputf_libs] {
	file mkdir RV_FPGA_PLC_Potato_iputf_libs
}

if {[file exists rtl_work]} {
	vdel -lib rtl_work -all
}
vlib rtl_work
vmap work rtl_work

###### Libraries for IPUTF cores 
###### End libraries for IPUTF cores 
###### MIF file copy and HDL compilation commands for IPUTF cores 


vcom "/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/clock_generator_sim/clock_generator.vho"

vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/RV_FPGA_PLC_Potato.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_soc_timer.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_soc_intercon.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_fifo.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_types.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_imm_decoder.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_csr.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_counter.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/aee_rom.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_soc_uart.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_writeback.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_wb_arbiter.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_csr_alu.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_constants.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_alu_mux.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_alu.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_utilities.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_fetch.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_alu_control_unit.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_soc_reset.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_soc_memory.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_wb_adapter.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_register_file.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_memory.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_icache.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_csr_unit.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_control_unit.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_comparator.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/aee_rom_wrapper.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_execute.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_decode.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_core.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/pp_potato.vhd}
vcom -93 -work work {/home/hossameldin/Documents/RV_FPGA_PLC_Project/Work/FPGA/RV_FPGA_PLC_Potato/vhdl/toplevel.vhd}

