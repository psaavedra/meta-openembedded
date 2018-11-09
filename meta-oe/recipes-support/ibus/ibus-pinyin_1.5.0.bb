DESCRIPTION = "PinYin engine for IBus."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
SECTION = "inputmethods"

SRC_URI = "https://github.com/ibus/${PN}/archive/${PV}.tar.gz \
          "
SRC_URI[md5sum] = "9a794c76f3e8fa888c7a0a097de2ebf0"
SRC_URI[sha256sum] = "79fc51517ab8dec696cbbbca9526459b59f4eb045492ea88e5f9e01160b905db"

inherit distro_features_check autotools lib_package

REQUIRED_DISTRO_FEATURES = "x11"

DEPENDS = "ibus enchant hunspell pyzy pyxdg"
DEPENDS_append_class-target = " intltool-native glib-2.0-native"
RDEPENDS_${PN} += " ibus enchant hunspell pyzy pyxdg"

BBCLASSEXTEND = "native"

EXTRA_OECONF += "--disable-static \
           --disable-boost \
"

FILES_${PN} += "${datadir}/ibus/component/pinyin.xml \
"

do_configure_prepend() {
    export PYTHON=python3
}
