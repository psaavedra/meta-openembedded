DESCRIPTION = "The Chinese PinYin and Bopomofo conversion library"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4b54a1fd55a448865a0b32d41598759d"
SECTION = "inputmethods"

SRCREV = "6d9c3cdff364e0da75e1c26222240f26370ebf73"
SRC_URI = "git://github.com/pyzy/pyzy.git;protocol=https;branch=master"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

DEPENDS = "glib-2.0 util-linux sqlite3"

EXTRA_OECONF += " \
    --disable-opencc \
    --disable-db-open-phrase \
    --disable-db-android \
"

BBCLASSEXTEND = "native"
