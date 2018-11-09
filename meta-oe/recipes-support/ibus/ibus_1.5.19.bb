DESCRIPTION = "A multilingual user input method library"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"
SECTION = "inputmethods"

SRC_URI = "https://github.com/ibus/ibus/releases/download/${PV}/${BPN}-${PV}.tar.gz \
          "
SRC_URI[md5sum] = "a2be6f200dd9ada2501474a6877a73ef"
SRC_URI[sha256sum] = "4b66c798dab093f0fa738e5c10688d395a463287d13678c208a81051af5d2429"

inherit autotools pkgconfig gettext gtk-immodules-cache gobject-introspection gtk-doc gtk-icon-cache gsettings

REQUIRED_DISTRO_FEATURES = "x11"

DEPENDS = " intltool dbus-glib python3-dbus iso-codes dconf libnotify python3-pygobject adwaita-icon-theme librsvg"
DEPENDS_append_class-target = " intltool-native python-pygobject-native"
RDEPENDS_${PN} += " iso-codes adwaita-icon-theme librsvg-gtk"

PACKAGECONFIG[gtk+] = ",--disable-gtk2,gtk+,gtk+"
PACKAGECONFIG[gtk+3] = ",--disable-gtk3,gtk+3,gtk+3"
PACKAGECONFIG[wayland] = "--enable-wayland,,wayland,wayland"

PACKAGECONFIG ?= "gtk+ gtk+3"
PACKAGECONFIG += "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)}"

GTKIMMODULES_PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'gtk+', 'ibus-gtk2.0', '', d)}"
GTKIMMODULES_PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'gtk+3', 'ibus-gtk3', '', d)}"

EXTRA_OECONF += " \
    --disable-emoji-dict \
    --disable-unicode-dict \
"

BBCLASSEXTEND = "native pythonnative"

FILES_${PN} += " \
    ${sysconfdir}/dconf/profile \
    ${sysconfdir}/dconf/db/ibus.d/00-upstream-settings \
    ${sysconfdir}/dconf/profile/ibus \
    ${libdir}/libibus-1.0.so.5.0.518 \
    ${libdir}/libibus-1.0.so.5 \
    ${libdir}/gtk-?.?/*/immodules/im-ibus.so \
    ${bindir}/ibus \
    ${bindir}/ibus-setup \
    ${bindir}/ibus-daemon \
    ${datadir}/ibus/engine \
    ${datadir}/ibus/component \
    ${datadir}/ibus/setup \
    ${datadir}/ibus/component/gtkpanel.xml \
    ${datadir}/ibus/component/simple.xml \
    ${datadir}/ibus/component/dconf.xml \
    ${datadir}/ibus/setup/emojilang.py \
    ${datadir}/ibus/setup/main.py \
    ${datadir}/ibus/setup/enginecombobox.py \
    ${datadir}/ibus/setup/setup.ui \
    ${datadir}/ibus/setup/enginedialog.py \
    ${datadir}/ibus/setup/i18n.py \
    ${datadir}/ibus/setup/engineabout.py \
    ${datadir}/ibus/setup/icon.py \
    ${datadir}/ibus/setup/keyboardshortcut.py \
    ${datadir}/ibus/setup/enginetreeview.py \
    ${datadir}/ibus/keymaps/jp \
    ${datadir}/ibus/keymaps/kr \
    ${datadir}/ibus/keymaps/common \
    ${datadir}/ibus/keymaps/modifiers \
    ${datadir}/ibus/keymaps/us \
    ${datadir}/ibus/keymaps/in \
    ${datadir}/applications/ibus-setup.desktop \
    ${datadir}/dbus-1/services/org.freedesktop.IBus.service \
    ${datadir}/dbus-1/services/org.freedesktop.portal.IBus.service \
    ${datadir}/vala/vapi/ibus-1.0.vapi \
    ${datadir}/vala/vapi/ibus-1.0.deps \
    ${libexecdir}/ibus-engine-simple \
    ${libexecdir}/ibus-portal \
    ${libexecdir}/ibus-dconf \
    ${libexecdir}/ibus-x11 \
    ${libexecdir}/ibus-ui-gtk3 \
    ${datadir}/GConf/gsettings/ibus.convert \
    ${datadir}/glib-2.0/schemas/org.freedesktop.ibus.gschema.xml \
    ${datadir}/bash-completion/* \
    ${datadir}/icons/* \
    ${libdir}/python?.?/????-packages/gi/overrides/IBus.py \
    ${libdir}/python?.?/????-packages/gi/overrides/IBus.py? \
"

do_configure_prepend() {
    export PYTHON=python3
}

do_install_append() {
  # Fix the installation path of the GObject Inspection overrides for Python3
  python_version=$(python3 -c 'import sys; print("%s.%s" % (sys.version_info.major, sys.version_info.minor))')
  mkdir -p ${D}/usr/lib/python${python_version}/site-packages/gi/overrides
  cp ${S}/bindings/pygobject/gi/overrides/IBus.py ${D}/usr/lib/python${python_version}/site-packages/gi/overrides/
  rm -rf ${D}/${RECIPE_SYSROOT_NATIVE}/usr/lib/python${python_version}/site-packages/gi/overrides
  find ${D} -type d -empty -delete
}
