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

TFA_PLATFORM = "rk3588"
TFA_BUILD_TARGET = "bl31"

SERIAL_CONSOLES = "115200;ttyFIQ0"

UBOOT_SUFFIX ?= "itb"
UBOOT_ENTRYPOINT ?= "0x06000000"
