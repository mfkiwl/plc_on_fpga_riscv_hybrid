/*
 *  Copyright 2019 Sergey Khabarov, sergeykhbr@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

#ifndef __AXI_MAPS_H__
#define __AXI_MAPS_H__

#include <inttypes.h>
#include "axi_const.h"
#include "maps/map_gpio.h"
#include "maps/map_gptimers.h"
#include "maps/map_uart.h"
#include "maps/map_irqctrl.h"
#include "maps/time_measurement.h"
#include "maps/i_o_peripheral.h"
#include "maps/timer_hw.h"
#include "maps/pwm_hw.h"
#include "maps/pid_hw.h"

#define ADDR_BUS0_XSLV_OTP          0x00010000
#define ADDR_BUS0_XSLV_FWIMAGE      0x00100000
#define ADDR_BUS0_XSLV_EXTFLASH     0x00200000
#define ADDR_BUS0_XSLV_SRAM         0x10000000
#define ADDR_BUS0_XSLV_GPIO         0x80000000
#define ADDR_BUS0_XSLV_UART1        0x80001000
#define ADDR_BUS0_XSLV_IRQCTRL      0x80002000
#define ADDR_BUS0_XSLV_GPTIMERS     0x80003000
#define ADDR_BUS0_XSLV_MEASUREMENT  0x80004000
#define ADDR_BUS0_XSLV_TON0         0x80005000
#define ADDR_BUS0_XSLV_PWM0         0x80006000
#define ADDR_BUS0_XSLV_PID0         0x80007000

// Interrupt pins assignemts:
#define CFG_IRQ_UNUSED      0
#define CFG_IRQ_UART1       1
#define CFG_IRQ_GPTIMERS    2
#define CFG_IRQ_TOTAL       3

// printf_uart enabling
#define PRINTF_UART_EN

#endif  // __AXI_MAPS_H__
