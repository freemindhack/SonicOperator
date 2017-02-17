/* ////////////////////////////////// "ippe.h" ///////////////////////////
//
//                  INTEL CORPORATION PROPRIETARY INFORMATION
//     This software is supplied under the terms of a license agreement or
//     nondisclosure agreement with Intel Corporation and may not be copied
//     or disclosed except in accordance with the terms of that agreement.
//         Copyright(c) 2014 Intel Corporation. All Rights Reserved.
//
//                 Intel(R) Integrated Performance Primitives
//                            Embedded functionality (ippE)
//
*/

#if !defined( __IPPE_H__ ) || defined( _OWN_BLDPCS )
#define __IPPE_H__


#if defined (_WIN32_WCE) && defined (_M_IX86) && defined (__stdcall)
  #define _IPP_STDCALL_CDECL
  #undef __stdcall
#endif


#ifndef __IPPDEFS_H__
#include "ippdefs.h"
#endif

#ifdef __cplusplus
extern "C" {
#endif


#if !defined( _IPP_NO_DEFAULT_LIB )
  #if defined( _IPP_SEQUENTIAL_DYNAMIC )
    #pragma comment( lib, "ippe" )
    #pragma comment( lib, "ippcore" )
  #elif defined( _IPP_SEQUENTIAL_STATIC )
    #pragma comment( lib, "ippemt" )
    #pragma comment( lib, "ippsmt" )
    #pragma comment( lib, "ippvmmt" )
    #pragma comment( lib, "ippcoremt" )
  #elif defined( _IPP_PARALLEL_DYNAMIC )
    #pragma comment( lib, "threaded/ippe" )
    #pragma comment( lib, "threaded/ippcore" )
  #elif defined( _IPP_PARALLEL_STATIC )
    #pragma comment( lib, "threaded/ippemt" )
    #pragma comment( lib, "threaded/ippsmt" )
    #pragma comment( lib, "threaded/ippvmmt" )
    #pragma comment( lib, "threaded/ippcoremt" )
  #endif
#endif

#if !defined( _OWN_BLDPCS )

#endif  /* _OWN_BLDPCS */



/* /////////////////////////////////////////////////////////////////////////////
//  Name:       ippeGetLibVersion
//  Purpose:    get the library version
//  Parameters:
//  Returns:    pointer to structure describing version of the ippE library
//
//  Notes:      don't free the pointer
*/
IPPAPI( const IppLibraryVersion*, ippeGetLibVersion, (void) )

/*LTE MIMO*/
#if !defined( _OWN_BLDPCS )
typedef struct {
     Ipp16sc symb[4];
} IppFourSymb;
#endif
IPPAPI(IppStatus, ippsMimoMMSE_1X2_16sc,(Ipp16sc* pSrcH[2],int srcHStride2, int srcHStride1, int srcHStride0, Ipp16sc* pSrcY[4][12], int Sigma2,IppFourSymb* pDstX, int dstXStride1, int dstXStride0,int numSymb, int numSC, int SINRIdx,Ipp32f* pDstSINR, int scaleFactor))
IPPAPI(IppStatus, ippsMimoMMSE_2X2_16sc,(Ipp16sc* pSrcH[2],int srcHStride2, int srcHStride1, int srcHStride0, Ipp16sc* pSrcY[4][12], int Sigma2,IppFourSymb* pDstX, int dstXStride1, int dstXStride0,int numSymb, int numSC, int SINRIdx,Ipp32f* pDstSINR, int scaleFactor))
IPPAPI(IppStatus, ippsMimoMMSE_1X4_16sc,(Ipp16sc* pSrcH[2],int srcHStride2, int srcHStride1, int srcHStride0, Ipp16sc* pSrcY[4][12], int Sigma2,IppFourSymb* pDstX, int dstXStride1, int dstXStride0,int numSymb, int numSC, int SINRIdx,Ipp32f* pDstSINR, int scaleFactor))
IPPAPI(IppStatus, ippsMimoMMSE_2X4_16sc,(Ipp16sc* pSrcH[2],int srcHStride2, int srcHStride1, int srcHStride0, Ipp16sc* pSrcY[4][12], int Sigma2,IppFourSymb* pDstX, int dstXStride1, int dstXStride0,int numSymb, int numSC, int SINRIdx,Ipp32f* pDstSINR, int scaleFactor))

/* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

#if defined (_IPP_STDCALL_CDECL)
  #undef  _IPP_STDCALL_CDECL
  #define __stdcall __cdecl
#endif


#ifdef __cplusplus
}
#endif

#endif /* __IPPE_H__ */
/* ////////////////////////// End of file "ippe.h" //////////////////// */