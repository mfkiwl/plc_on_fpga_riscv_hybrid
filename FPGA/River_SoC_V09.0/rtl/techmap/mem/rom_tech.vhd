--!
--! Copyright 2019 Sergey Khabarov, sergeykhbr@gmail.com
--!
--! Licensed under the Apache License, Version 2.0 (the "License");
--! you may not use this file except in compliance with the License.
--! You may obtain a copy of the License at
--!
--!     http://www.apache.org/licenses/LICENSE-2.0
--!
--! Unless required by applicable law or agreed to in writing, software
--! distributed under the License is distributed on an "AS IS" BASIS,
--! WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--! See the License for the specific language governing permissions and
--! limitations under the License.
--!

library ieee;
use ieee.std_logic_1164.all;
--library techmap;
use work.gencomp.all;
use work.types_mem.all;
--library commonlib;
use work.types_common.all;
--! AMBA system bus specific library
--library ambalib;
--! AXI4 configuration constants.
use work.types_amba4.all;


LIBRARY altera_mf;
USE altera_mf.altera_mf_components.all;

entity Rom_tech is
generic (
    memtech : integer := 0;
    abits : integer;
    sim_hexfile : string;
	 cyc_miffile : string
);
port (
    clk       : in std_logic;
    address   : in global_addr_array_type;
    data      : out std_logic_vector(CFG_SYSBUS_DATA_BITS-1 downto 0)
);
end;

architecture rtl of Rom_tech is

  component Rom_inferred is
  generic (
    abits : integer;
    hex_filename : string
  );
  port (
    clk     : in  std_ulogic;
    address : in global_addr_array_type;
    data    : out std_logic_vector(CFG_SYSBUS_DATA_BITS-1 downto 0)
  );
  end component;

begin

	genrom0 : if memtech = inferred or (is_fpga(memtech) /= 0 and memtech /= cyclone_v) generate
      infer0 : Rom_inferred  generic map (abits, sim_hexfile)
               port map (clk, address, data);
	end generate;
  
	genrom1 : if memtech = inferred or (is_fpga(memtech) /= 0 and memtech = cyclone_v) generate
		altsyncram_component : altsyncram
		GENERIC MAP (
			address_aclr_a => "NONE",
			clock_enable_input_a => "BYPASS",
			clock_enable_output_a => "BYPASS",
			init_file => cyc_miffile,
			intended_device_family => "Cyclone V",
			lpm_hint => "ENABLE_RUNTIME_MOD=NO",
			lpm_type => "altsyncram",
			numwords_a => 2**(abits-3),
			operation_mode => "ROM",
			outdata_aclr_a => "NONE",
			outdata_reg_a => "UNREGISTERED",
			widthad_a => abits-3,
			width_a => CFG_SYSBUS_DATA_BITS,
			width_byteena_a => 1
		)
		PORT MAP (
			address_a => address(0)(abits-1 downto 3),
			clock0 => clk,
			q_a => data
		);
	end generate;

end; 


