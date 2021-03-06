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

--library commonlib;
use work.types_common.all;

--library techmap;
use work.gencomp.all;
use work.types_mem.all;

entity dpram_tech is
generic (
    memtech : integer := 0;
    abits   : integer := 12;
    dbits   : integer := 64
);
port (
    i_clk   : in std_logic;
    i_raddr : in std_logic_vector(abits-1 downto 0);
    o_rdata : out std_logic_vector(dbits-1 downto 0);
    i_waddr : in std_logic_vector(abits-1 downto 0);
    i_wena  : in std_logic;
    i_wdata : in std_logic_vector(dbits-1 downto 0)
);
end;

architecture rtl of dpram_tech is

  component dpram_inferred is
  generic (
     abits : integer := 12;
     dbits : integer := 64
  );
  port (
    i_clk   : in std_logic;
    i_raddr : in std_logic_vector(abits-1 downto 0);
    o_rdata : out std_logic_vector(dbits-1 downto 0);
    i_waddr : in  std_logic_vector(abits-1 downto 0);
    i_wena  : in std_logic;
    i_wdata : in std_logic_vector(dbits-1 downto 0)
  );
  end component;

begin

  inf0 : if memtech = inferred or is_fpga(memtech) /= 0 generate
      x0 : dpram_inferred generic map 
      (
          abits => abits,
          dbits => dbits
      ) port map (
          i_clk   => i_clk,
          i_raddr => i_raddr,
          o_rdata => o_rdata,
          i_waddr => i_waddr,
          i_wena  => i_wena,
          i_wdata => i_wdata
      );
  end generate;

end; 


