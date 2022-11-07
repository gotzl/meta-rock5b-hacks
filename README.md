# meta-rock5b-hacks

This README file contains information on the contents of the meta-rock5b-hacks layer.
The layer targets the [Radxa Rock5b board](https://wiki.radxa.com/Rock5/5b).

## Dependencies

This layer depends on:

[meta-rockchip](https://git.yoctoproject.org/meta-rockchip) (the git.yoctoproject.org one)
* branch: kirkstone

## Table of Contents

  I. Configure yocto/oe environment    
 II. Changes for the Rock5b implemented in this layer    
 III. Booting your device    

### I. Configure yocto/oe environment
This layer adds support for the Radxa Rock5b, which is not fully supported by
the meta-rockchip layer. To build for this device, use

```
MACHINE ?= "rock5b"
```

in your local.conf file.

### II. Changes for the Rock5b implemented in this layer
(I assume here that the Rock5b is equiped with an eMMC)

First, this layer uses the linux-rockchip kernel fork of radxa as well as their u-boot and rkbin forks. Since this version
of u-boot seems to be unamble to load fitImage, some modifications are made to get plain Image and .dtb on the boot partition.

### III. Booting your device
Short version of [official instructions](https://wiki.radxa.com/Rock5/install/usb-install-emmc).

Get the [rkdeveloptool](https://github.com/radxa/rkdeveloptool.git). Power the device while pressing the button next to the fan pins. Then
```
rkdeveloptool ld
rkdeveloptool db latest/rk3588_spl_loader_v1.08.111.bin
rkdeveloptool wl 0 /path/to/image.wic
rkdeveloptool rd
```
