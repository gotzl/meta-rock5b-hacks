FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
KBRANCH ?= "stable-5.10-rock5"

require recipes-kernel/linux/linux-yocto.inc

SRCREV = "7917720a4d4dc4f1e37feaa16698773ce8f2d230"
SRCREV_machine ?= "7917720a4d4dc4f1e37feaa16698773ce8f2d230"
SRCREV_meta ?= "96ea2660bb97e15f48f4885b9e436f24c3606bd9"

SRC_URI = " \
	git://github.com/radxa/kernel.git;protocol=https;nobranch=1;branch=${KBRANCH}; \
	git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.10;destsuffix=${KMETA}; \
	file://rockchip_wlan.cfg \
"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION = "5.10.66"

EPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native libmpc-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"


COMPATIBLE_MACHINE:rock5b = "rock5b"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"

# FIXME: needed until we can use u-boot mainline where fitImages work
KERNEL_IMAGETYPES:append = " Image"
