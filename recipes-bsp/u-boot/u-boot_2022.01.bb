FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

SRCREV = "77a5f377ab102272081ac6094a3e5892276de7d6"
SRCREV_rkbin = "d6aad64d4874b416f25669748a9ae5592642a453"
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

    oe_runmake -C ${S} O=${B}/${config} BL31=${B}/../rkbin/bin/rk35/${BOOT_LOADER} \
        spl/u-boot-spl.bin u-boot.dtb u-boot.itb
    ${B}/tools/mkimage -n rk3588 -T rksd -d ${B}/../rkbin/bin/rk35/${SPL_FILE}:spl/u-boot-spl.bin idbloader.img
}

do_deploy:append() {
	install ${B}/../rkbin/bin/rk35/${SPL_LOADER} ${DEPLOYDIR}/${SPL_LOADER}
}

DEPENDS += "bc-native dtc-native python3-setuptools-native coreutils-native"
