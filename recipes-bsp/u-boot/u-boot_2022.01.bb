FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

SRCREV = "49da44e116d1f68f0ce187d551a7b5b9d1571768"
SRCREV_rkbin = "9840e87723eef7c41235b89af8c049c1bcd3d133"
SRC_URI = " \
    git://github.com/radxa/u-boot.git;protocol=https;branch=stable-5.10-rock5; \
    git://github.com/radxa/rkbin.git;protocol=https;branch=master;name=rkbin;destsuffix=rkbin; \
    file://rkboot-ini.patch;patchdir=../rkbin \
    file://defconfig.patch \
    file://fdtfile-path.patch \
"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

do_configure:prepend() {
    # use python3
    sed -i -e 's,python2,python3,g' ${S}/make.sh ${S}/arch/arm/mach-rockchip/decode_bl31.py
    # fix paths
    sed -i -e 's,./${srctree},${srctree},g' \
        ${S}/arch/arm/mach-rockchip/fit_nodes.sh \
        ${S}/arch/arm/mach-rockchip/make_fit_atf.sh
}

do_compile:append() {
    cd ${B}

    # Prepare needed files
    for d in make.sh scripts configs arch/arm/mach-rockchip; do
        cp -rT ${S}/${d} ${d}
    done

    oe_runmake -C ${S} O=${B}/${config} BL31=${B}/../rkbin/bin/rk35/rk3588_bl31_v1.28.elf \
        spl/u-boot-spl.bin u-boot.dtb u-boot.itb
    ${B}/tools/mkimage -n rk3588 -T rksd -d ${B}/../rkbin/bin/rk35/rk3588_ddr_lp4_2112MHz_lp5_2736MHz_v1.08.bin:spl/u-boot-spl.bin idbloader.img
}

DEPENDS += "bc-native dtc-native python3-setuptools-native coreutils-native"
