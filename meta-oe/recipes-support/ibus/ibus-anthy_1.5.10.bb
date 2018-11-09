DESCRIPTION = "It is a Japanese input engine for IBus."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
SECTION = "inputmethods"

SRC_URI = "https://github.com/ibus/${PN}/releases/download/${PV}/${PN}-${PV}.tar.gz \
    file://fix_anthy_include_in_gir.patch \
"
SRC_URI[md5sum] = "d7c2c410619edacfbaca6a9d02ed8982"
SRC_URI[sha256sum] = "cc33e0d1afe7e4a1f0a092d2a4575269ac21373771e49579c434afb4eef9f84a"

# Needed to find the Anthy included headers (anthy/anthy.h)
export STAGING_DIR_HOST

inherit autotools pkgconfig gettext gobject-introspection gtk-immodules-cache gtk-icon-cache gsettings qemu

REQUIRED_DISTRO_FEATURES = "x11"

DEPENDS = " anthy ibus gobject-introspection adwaita-icon-theme"
RDEPENDS_${PN} += " ibus takao-fonts anthy libanthy0"

DEPENDS_append_class-target = " intltool-native qemu-native"

do_configure_prepend() {
    export PYTHON=python2
}


FILES_${PN} += " \
    ${datadir}/icons/* \
    ${datadir}/ibus \
    ${datadir}/appdata \
    ${datadir}/ibus/component/anthy.xml \
    ${datadir}/appdata/ibus-anthy.appdata.xml \
    ${datadir}/gir-1.0/Anthy-9000.gir \
    ${datadir}/glib-2.0/schemas/org.freedesktop.ibus.engine.anthy.gschema.xml \
"
