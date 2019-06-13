// The Potato Processor Benchmark Applications
// (c) Kristian Klomsten Skordal 2015 <kristian.skordal@wafflemail.net>
// Report bugs and issues on <https://github.com/skordal/potato/issues>

#include <stdint.h>

#include "platform.h"
#include "uart.h"
#include "gpio.h"

static struct uart uart0;
static struct gpio module;

void exception_handler(uint32_t cause, void * epc, void * regbase)
{
	// Not used in this application
}

int main(void)
{
	uart_initialize(&uart0, (volatile void *) PLATFORM_UART0_BASE);
	uart_set_divisor(&uart0, uart_baud2divisor(115200, PLATFORM_SYSCLK_FREQ));
	uart_tx_string(&uart0, "Hi GPIO\n\r");
    
    gpio_initialize(&module, (void*) 0xc0004000);
    gpio_set_output(&module, 0xF00);
    gpio_set_pin(&module, 0);

	return 0;
}

