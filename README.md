# PNG

## Chunk layout
first 8 bytes of a PNG 

```text
    89 50 4e 47 0d 0a 1a 0a
```

1. 4 bytes for data size
1. 4 bytes for type code
1. data
1. 4 byte CRC

for type code the 5th bit of each byte -> upper/lower
1. Ancillary bit    : 0 (uppercase) = critical, 1 (lowercase) = ancillary.
2. Private bit      : 0 (uppercase) = public, 1 (lowercase) = private.
3. Reserved bit     : Must be 0 (uppercase) in files conforming to this version of PNG.
4. Safe-to-copy bit : 0 (uppercase) = unsafe to copy, 1 (lowercase) = safe to copy.
(1, 2, 3, 4)

### CRC algorithm

## Chunk Specifications
### Critical chunks
#### IHDR Image header
The IHDR chunk must appear FIRST. It contains(bytes):
1. Width               : 4
1. Height              : 4
1. Bit depth           : 1
1. Color type          : 1
1. Compression method  : 1
1. Filter method       : 1
1. Interlace method    : 1

#### PLTE Palette 调色板

#### IDAT Image data

IDAT 

解码:
    先LZ77
    每一行第一位定义filter type, 
    
    LZ77 uncompress后总共有height*width*pixelSize + height bytes
    
    执行filter Algorithm
    


An additional "filter-type" byte is added to the beginning of every scanline (see Filtering). 
The filter-type byte is not considered part of the image data, but it is included in 
the datastream sent to the compression step.



#### IEND Image trailer
end of image
00 00 00 00 49 45 4e 44 ae 42  60 82 


### Ancillary chunks
#### Transparency information
#### Color space information
gAMA Image gamma

cHRM Primary chromaticities

sRGB Standard RGB color space

iCCP Embedded ICC profile

#### Textual information
tEXt Textual data 

zTXt Compressed textual data

iTXt International textual data

### Miscellaneous information
bKGD Background color

pHYs Physical pixel dimensions

sBIT Significant bits

sPLT Suggested palette

hIST Palette histogram

tIME Image last-modification time

## Filter Algorithms
Filtering algorithms are applied to bytes,

filter types
- 0 None
- 1 Sub
- 2 Up
- 3 Average
- 4 Paeth

## Chunk Ordering Rules



for decoder suggest
An un-known chunk type is not to be treated as an error unless it is a critical chunk.

