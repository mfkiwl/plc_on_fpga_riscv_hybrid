library IEEE;
USE IEEE.STD_LOGIC_1164.ALL;
USE IEEE.STD_LOGIC_SIGNED.ALL;

entity PWM_32_bit is
port( clk, reset	: in STD_LOGIC;
		EN				: in STD_LOGIC;
		T_Count		: in STD_LOGIC_VECTOR(31 downto 0);
		Comp_Count	: in STD_LOGIC_VECTOR(31 downto 0);
		Y_OUT			: out STD_LOGIC
		);
end;

architecture RTL of PWM_32_bit is
	SIGNAL T_Count_T, Comp_Count_T	: STD_LOGIC_VECTOR(31 downto 0);
	
	begin
	
	process(clk, reset)
		begin
		if (rising_edge(clk) and EN = '1') then
			if reset = '1' then 
				T_Count_T		<= (others => '0');
				Comp_Count_T	<= (others => '0');
				Y_OUT				<= '0';
			elsif T_Count_T = X"00000000" then
				T_Count_T		<= T_Count;
				Comp_Count_T	<= Comp_Count;
				Y_OUT				<= '0';
			else
				T_Count_T	<= T_Count_T - X"00000001";
				if T_Count_T < Comp_Count_T then
					Y_OUT <= '1';
				else
					Y_OUT <= '0';
				end if;
			end if;
		end if;
	end process;
	
end RTL;