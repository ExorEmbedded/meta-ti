DESCRIPTION = "Userspace libraries for SGX"
HOMEPAGE = "http://downloads.ti.com/dsps/dsps_public_sw/gfxsdk"
LICENSE = "TI-TSPA"
LIC_FILES_CHKSUM = "file://OMAP5-Linux-Graphics-DDK-UM-Manifest.doc;md5=360d293df455e4f2d363bb4014a49603"

PACKAGE_ARCH = "${MACHINE_ARCH}"

BRANCH_omap-a15 = "master"
BRANCH_ti33x = "am4/k4.1"
BRANCH_ti43x = "am4/k4.1"

SRC_URI = "git://git.ti.com/graphics/omap5-sgx-ddk-um-linux.git;protocol=git;branch=${BRANCH}"
SRCREV_omap-a15 = "d63cd6469fff610317a8e1c986f227bb3e7922f6"
SRCREV_ti33x    = "188575c7ba16c00a15499ed88a126af2506fdba5"
SRCREV_ti43x    = "fdf4c0b594ca142a68aabb4c3f82b75be50b46cc"

INITSCRIPT_NAME = "pvr-init"
INITSCRIPT_PARAMS = "defaults 8"

inherit update-rc.d

PR = "r12"
PROVIDES += "virtual/egl virtual/libgles1 virtual/libgles2"

RDEPENDS_${PN} += "libdrm"

RREPLACES_${PN} = "libegl libgles1 libgles2"
RREPLACES_${PN}-dev = "libegl-dev libgles1-dev libgles2-dev"
RREPLACES_${PN}-dbg = "libegl-dbg"

S = "${WORKDIR}/git"

SRC_URI_append = " \
    file://rc.pvr \
    file://powervr.ini \
"

do_install () {
    oe_runmake install DESTDIR=${D}
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/rc.pvr ${D}${sysconfdir}/init.d/pvr-init

    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/powervr.ini ${D}${sysconfdir}/
}

FILES_${PN} =  "${bindir}/*"
FILES_${PN} += " ${libdir}/*"
FILES_${PN} +=  "${includedir}/* /usr/share/sgx-lib/"
FILES_${PN} += "${sysconfdir}/init.d/pvr-init"
FILES_${PN} += "${sysconfdir}/powervr.ini"

PACKAGES =+ "${PN}-plugins"
FILES_${PN}-plugins = ""
FILES_${PN}-plugins_ti43x = "${libdir}/libsrv_init.so ${libdir}/libsrv_um.so ${libdir}/libglslcompiler.so ${libdir}/libpvrws_WAYLAND.so ${libdir}/libpvrws_KMS.so "
RDEPENDS_${PN} += "${PN}-plugins"

ALLOW_EMPTY_${PN}-plugins = "1"

INHIBIT_PACKAGE_STRIP = "1"

INSANE_SKIP_${PN} += "dev-so ldflags useless-rpaths"
INSANE_SKIP_${PN}-plugins = "dev-so"

CLEANBROKEN = "1"
