DESCRIPTION = "It is a Japanese input engine for IBus."
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=86bfc594f9971fb2797f5aea1a49d316"
SECTION = "inputmethods"

SRC_URI = "https://github.com/definite/${PN}/archive/${PV}.tar.gz \
           file://0001-revert-CMakeLists.txt.patch \
           file://0002-add-Modules.patch \
           file://0003-add-ChangeLog.prev.patch \
           file://0005-fix-cmake-cross-compile.patch \
"

SRC_URI[md5sum] = "6360649580363bb3627a11e32057f6b1"
SRC_URI[sha256sum] = "ef62c22ef4e8f9085fc40fcbc14c30f6dac458817df98e9f90f883a3e2080089"

inherit cmake

DEPENDS = " ibus-table "

BBCLASSEXTEND = "native"

# The supported options are "Unix Makefiles" or "Ninja".
OECMAKE_GENERATOR = "Unix Makefiles"

cmake_do_generate_toolchain_file() {
    if [ "${BUILD_SYS}" = "${HOST_SYS}" ]; then
        cmake_crosscompiling="set( CMAKE_CROSSCOMPILING FALSE )"
    fi
    cat > ${WORKDIR}/toolchain.cmake <<EOF
# CMake system name must be something like "Linux".
# This is important for cross-compiling.
$cmake_crosscompiling
set( CMAKE_SYSTEM_NAME `echo ${TARGET_OS} | sed -e 's/^./\u&/' -e
's/^\(Linux\).*/\1/'` )
set( CMAKE_SYSTEM_PROCESSOR
${@map_target_arch_to_uname_arch(d.getVar('TARGET_ARCH'))} )
set( CMAKE_C_COMPILER ${OECMAKE_C_COMPILER} )
set( CMAKE_CXX_COMPILER ${OECMAKE_CXX_COMPILER} )
set( CMAKE_ASM_COMPILER ${OECMAKE_C_COMPILER} )
set( CMAKE_AR ${OECMAKE_AR} CACHE FILEPATH "Archiver" )
set( CMAKE_C_FLAGS "${OECMAKE_C_FLAGS}" CACHE STRING "CFLAGS" )
set( CMAKE_CXX_FLAGS "${OECMAKE_CXX_FLAGS}" CACHE STRING "CXXFLAGS" )
set( CMAKE_ASM_FLAGS "${OECMAKE_C_FLAGS}" CACHE STRING "ASM FLAGS" )
set( CMAKE_C_FLAGS_RELEASE "${OECMAKE_C_FLAGS_RELEASE}" CACHE STRING
"Additional CFLAGS for release" )
set( CMAKE_CXX_FLAGS_RELEASE "${OECMAKE_CXX_FLAGS_RELEASE}" CACHE STRING
"Additional CXXFLAGS for release" )
set( CMAKE_ASM_FLAGS_RELEASE "${OECMAKE_C_FLAGS_RELEASE}" CACHE STRING
"Additional ASM FLAGS for release" )
set( CMAKE_C_LINK_FLAGS "${OECMAKE_C_LINK_FLAGS}" CACHE STRING "LDFLAGS" )
set( CMAKE_CXX_LINK_FLAGS "${OECMAKE_CXX_LINK_FLAGS}" CACHE STRING "LDFLAGS" )

# only search in the paths provided so cmake doesnt pick
# up libraries and tools from the native build machine
set( CMAKE_FIND_ROOT_PATH ${STAGING_DIR_HOST} ${STAGING_DIR_NATIVE}
${CROSS_DIR} ${OECMAKE_PERLNATIVE_DIR} ${OECMAKE_EXTRA_ROOT_PATH}
${EXTERNAL_TOOLCHAIN})
set( CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY )
# set( CMAKE_FIND_ROOT_PATH_MODE_PROGRAM ${OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM} )
set( CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY )
# set( CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY )

# Use qt.conf settings
set( ENV{QT_CONF_PATH} ${WORKDIR}/qt.conf )

# We need to set the rpath to the correct directory as cmake does not provide
# any
# directory as rpath by default
set( CMAKE_INSTALL_RPATH ${OECMAKE_RPATH} )

# Use native cmake modules
list(APPEND CMAKE_MODULE_PATH "${STAGING_DATADIR}/cmake/Modules/")

# add for non /usr/lib libdir, e.g. /usr/lib64
set( CMAKE_LIBRARY_PATH ${libdir} ${base_libdir})

EOF
}

FILES_${PN} += "${datadir}/ibus-table/icons/* \
    ${datadir}/ibus-table/tables/* \
"
