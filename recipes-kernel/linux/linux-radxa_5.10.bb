FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
KBRANCH ?= "linux-5.10-gen-rkr3.4"

require recipes-kernel/linux/linux-yocto.inc

SRCREV = "b62cf4be15ea8a8fd2dc980ae39c36dc42f2f065"
SRCREV_machine ?= "b62cf4be15ea8a8fd2dc980ae39c36dc42f2f065"
SRCREV_meta ?= "96ea2660bb97e15f48f4885b9e436f24c3606bd9"

SRC_URI = " \
	git://github.com/radxa/kernel.git;protocol=https;nobranch=1;branch=${KBRANCH}; \
	git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.10;destsuffix=${KMETA}; \
	file://various.cfg \
	file://docker-optional.cfg \
"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION = "5.10.110"

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

do_compile_kernelmodules:prepend() {
	# FIXME: hack to get rkwifi modules build with buildroot separated from srcroot
	sed -i -e 's, \$(src), '${S}'/\$(src),g' \
	       -e 's,-I\$(src),-I'${S}'/\$(src),g' \
		${S}/drivers/net/wireless/broadcom/brcm80211/brcmutil/Makefile \
		${S}/drivers/net/wireless/broadcom/brcm80211/brcmfmac/Makefile \
		${S}/drivers/net/wireless/rockchip_wlan/rkwifi/bcmdhd/Makefile \
		${S}/drivers/net/wireless/rockchip_wlan/rtl8852be/Makefile \
		${S}/drivers/net/wireless/rockchip_wlan/rtl8852be/common.mk \
		${S}/drivers/net/wireless/rockchip_wlan/rtl8852bu/Makefile \
		${S}/drivers/net/wireless/rockchip_wlan/rtl8852bu/common.mk
	export TopDIR=${S}
}
