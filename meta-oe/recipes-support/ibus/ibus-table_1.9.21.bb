DESCRIPTION = "framework for table based input methods using IBus"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"
SECTION = "inputmethods"

SRC_URI = "https://github.com/kaio/${PN}/releases/download/${PV}/${PN}-${PV}.tar.gz \
          "
SRC_URI[md5sum] = "1f7b3fab7271e409c9ef4c43f5c3fc78"
SRC_URI[sha256sum] = "017bcd1cb9f5a79df46d1e4ac28f166ce2338938a2037ebe86783da0abca0c24"

inherit distro_features_check autotools gettext lib_package gsettings

DEPENDS = " ibus"
RDEPENDS_${PN} += " ibus"

BBCLASSEXTEND = "native"

do_configure_prepend() {
    export PYTHON=python3
}

FILES_${PN} += "${datadir}/ibus/component/pinyin.xml \
  ${datadir}/metainfo \
  ${datadir}/ibus \
  ${datadir}/glib-2.0 \
  ${datadir}/metainfo/ibus-table.appdata.xml \
  ${datadir}/ibus/component/table.xml \
  ${datadir}/glib-2.0/schemas/org.freedesktop.ibus.engine.table.gschema.xml \
"
