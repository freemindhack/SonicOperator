#pragma once
#include <stdlib.h>

#ifdef __cplusplus
extern "C"
{
#endif


typedef struct
{
    float real;
    float imag;
}
CkFftComplex;


typedef struct _CkFftContext CkFftContext;


typedef enum 
{
    kCkFftDirection_Forward = (1 << 0),
    kCkFftDirection_Inverse = (1 << 1),
    kCkFftDirection_Both    = (kCkFftDirection_Forward | kCkFftDirection_Inverse)
}
CkFftDirection;


// Create an FFT context.
//
// Parameters:
//   nMax:       Maximum number of elements in the FFTs to be performed with this 
//               context; must be a power of 2.
//   direction:  Direction of the FFTs to be performed with this context.
//   buf:        Optional memory buffer in which to allocate the context.
//   bufSize:    Optional pointer to size of memory buffer, in bytes.
//
// Any FFT requires a context.  Contexts can (and should) be used for multiple FFTs.
// The context does not contain state, so contexts can be used simultaneously on 
// different threads.
//
// If you are content to let CkFftInit() allocate its own memory, pass in NULL for
// both buf and bufSize.  
//
// If buf is not NULL, and the value pointed to by bufSize is large enough, then
// then the FFT will use the buffer for the context.  If the value pointed by bufSize 
// is not large enough, then the minimum number of bytes required will be placed in 
// *bufSize.
// 
// For example:
//   size_t memSize = 0;
//   CkFftInit(nMax, kCkFftDirection_Forward, NULL, &memSize);
//   void* mem = malloc(memSize);
//   CkFftContext* context = CkFftInit(nMax, kCkFftDirection_Forward, mem, &memSize);
//
// Returns a context pointer if one could be created, or NULL if not.
//
CkFftContext* CkFftInit(int nMax, CkFftDirection direction, void* buf, size_t* bufSize);



// Perform a forward FFT on real data.
//
// Parameters:
//   context: A context pointer from CkFftInit().
//   n:       The size of the FFT.  This must be a power of 2 and must not be greater
//            than the value of nMax specified when the context was created.
//   input:   Real input data, containing n elements.
//   output:  Buffer for complex output data, containing n/2+1 elements.  
// 
// Only n/2+1 output values are returned, rather than the full n, because the output 
// values obey the symmetry relation:
//   output[i].real = output[n-i].real
//   output[i].imag = -output[n-i].imag
//
// The FFT is NOT performed in-place, so input and output must be different buffers.
// 
// No scaling is applied to the results of either the forward or inverse FFT, so if you 
// apply a forward FFT followed by an inverse FFT to a set of real data, the result is
// the original data, scaled by 2*n.
// 
// Returns 1 if the FFT could be performed, or 0 if one of the parameters was invalid.
//
int CkFftRealForward(CkFftContext* context, int n, const float* input, CkFftComplex* output);



// Perform an inverse FFT on the result of a forward FFT on real data.
//
// Parameters:
//   context: A context pointer from CkFftInit().
//   n:       The size of the FFT.  This must be a power of 2 and must not be greater
//            than the value of nMax specified when the context was created.
//   input:   Complex input data, containing n/2+1 elements. This should be data that
//            was obtained by a call to CkFftRealForward().
//   output:  Buffer for real output data, containing n float elements.  
//   tmpBuf:  A temporary buffer, containing n/2+1 complex elements.
// 
// The FFT is NOT performed in-place, so input and output must be different buffers.
// 
// No scaling is applied to the results of either the forward or inverse FFT, so if you 
// apply a forward FFT followed by an inverse FFT to a set of real data, the result is
// the original data, scaled by 2*n.
// 
// Returns 1 if the FFT could be performed, or 0 if one of the parameters was invalid.
//
int CkFftRealInverse(CkFftContext* context, int n, const CkFftComplex* input, float* output, CkFftComplex* tmpBuf);



// Perform a forward FFT on complex data.
//
// Parameters:
//   context: A context pointer from CkFftInit().
//   n:       The size of the FFT.  This must be a power of 2 and must not be greater
//            than the value of nMax specified when the context was created.
//   input:   Complex input data, containing n elements.
//   output:  Buffer for complex output data, containing n elements.
//
// The FFT is NOT performed in-place, so input and output must be different buffers.
// 
// No scaling is applied to the results of either the forward or inverse FFT, so if you 
// apply a forward FFT followed by an inverse FFT to a set of data, the result is
// the original data, scaled by n.
// 
// Returns 1 if the FFT could be performed, or 0 if one of the parameters was invalid.
//
int CkFftComplexForward(CkFftContext* context, int n, const CkFftComplex* input, CkFftComplex* output);



// Perform an inverse FFT on complex data.
//
// Parameters:
//   context: A context pointer from CkFftInit().
//   n:       The size of the FFT.  This must be a power of 2 and must not be greater
//            than the value of nMax specified when the context was created.
//   input:   Complex input data, containing n elements.
//   output:  Buffer for complex output data, containing n elements.
//
// The FFT is NOT performed in-place, so input and output must be different buffers.
// 
// No scaling is applied to the results of either the forward or inverse FFT, so if you 
// apply a forward FFT followed by an inverse FFT to a set of data, the result is
// the original data, scaled by n.
// 
// Returns 1 if the FFT could be performed, or 0 if one of the parameters was invalid.
//
int CkFftComplexInverse(CkFftContext* context, int n, const CkFftComplex* input, CkFftComplex* output);



// Destroy an FFT context.
//
// If you let CkFftInit() allocate its own memory buffer, then this will free that buffer.
//
void CkFftShutdown(CkFftContext*);








#ifdef __cplusplus
} // extern "C"
#endif
