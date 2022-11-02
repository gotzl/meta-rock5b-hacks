# Copyright (C) 2020 Garmin Ltd. or its subsidaries
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY = "rk3588"
PREFERRED_VERSION_linux-radxa = "5.10%"
PREFERRED_PROVIDER_virtual/kernel = "linux-radxa"

DEFAULTTUNE ?= "cortexa76"

require conf/machine/include/soc-family.inc
require conf/machine/include/arm/armv8-2a/tune-cortexa76.inc
require conf/machine/include/rockchip-defaults.inc
require conf/machine/include/rockchip-wic.inc

MALI_GPU := "valhall-g610"
MALI_VERSION := "g6p0"

KBUILD_DEFCONFIG ?= "rockchip_linux_defconfig"
KERNEL_CLASSES = "kernel-fitimage"
KERNEL_IMAGETYPE = "fitImage"

BOOT_LOADER ?= "rk3588_bl31_v1.28.elf"
SPL_FILE ?= "rk3588_ddr_lp4_2112MHz_lp5_2736MHz_v1.08.bin"
SPL_LOADER ?= "rk3588_spl_loader_v1.08.111.bin"

SERIAL_CONSOLES = "115200;ttyFIQ0"

# Docker optimizations
APPEND += "cgroup_enable=cpuset cgroup_memory=1 cgroup_enable=memory swapaccount=1"
# For RK3588
APPEND += "irqchip.gicv3_pseudo_nmi=0 switolb=1 coherent_pool=2M"

UBOOT_SUFFIX ?= "itb"
UBOOT_ENTRYPOINT ?= "0x06000000"
