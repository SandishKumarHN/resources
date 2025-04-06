# PyTorch GPU Components Syllabus (C++ Focus)

This syllabus is designed for a Staff Distributed Systems Engineer at Meta who aims to become an open-source contributor to PyTorch. It focuses specifically on the GPU-related components of PyTorch, providing a structured learning path with recommended videos (under 30 minutes) and C++-focused blog posts for each topic.

## Module 1: PyTorch CUDA Fundamentals

### Topic 1.1: torch.cuda Basics
- **Concepts:**
  - CUDA device management in PyTorch
  - Checking CUDA availability
  - Device selection and context management
  - Synchronization between CPU and GPU operations

- **Recommended Resources:**
  - **Video:** [Lecture 1 How to profile CUDA kernels in PyTorch](https://www.youtube.com/watch?v=LuhJEEJQgUM) by GPU MODE (relevant sections, under 30 min)
  - **Blog Post:** [PyTorch C++ API](https://pytorch.org/cppdocs/) - Official PyTorch C++ API documentation

### Topic 1.2: CUDA Tensors
- **Concepts:**
  - Creating tensors on GPU
  - Moving tensors between CPU and GPU
  - Memory management for CUDA tensors
  - Basic operations with CUDA tensors

- **Recommended Resources:**
  - **Video:** [Tutorial 2: Introduction to PyTorch (Part 1)](https://www.youtube.com/watch?v=wnKZZgFQY-E) (CUDA sections, under 30 min)
  - **Blog Post:** [PyTorch internals](https://blog.ezyang.com/2019/05/pytorch-internals/) by Edward Z. Yang

### Topic 1.3: Memory Management
- **Concepts:**
  - CUDA memory allocator
  - Caching allocator for efficient memory reuse
  - Memory pools and fragmentation handling
  - Custom memory allocators

- **Recommended Resources:**
  - **Video:** [CUDA Tooling and Basic Optimizations](https://www.youtube.com/watch?v=9E9eqO864Aw) (memory sections, under 30 min)
  - **Blog Post:** [Moving model to CUDA in C++](https://discuss.pytorch.org/t/moving-model-to-cuda-in-c/26786) from PyTorch Forums

## Module 2: Accelerated Neural Network Operations

### Topic 2.1: cuDNN Integration
- **Concepts:**
  - Deep Neural Network library by NVIDIA
  - Optimized implementations for neural network operations
  - Convolutions, pooling, and normalization
  - cuDNN configuration options in PyTorch

- **Recommended Resources:**
  - **Video:** [10. CUDA C++ Basics](https://m.youtube.com/watch?v=ymsUskSjIAg) by NERSC (cuDNN sections, under 30 min)
  - **Blog Post:** [Error using CUDNN in custom cuda file](https://discuss.pytorch.org/t/error-using-cudnn-in-custom-cuda-file/137141) from PyTorch Forums

### Topic 2.2: cuBLAS Integration
- **Concepts:**
  - Basic Linear Algebra Subprograms for CUDA
  - Matrix multiplications and linear algebra operations
  - Performance optimization for different GPU architectures
  - cuBLAS configuration in PyTorch

- **Recommended Resources:**
  - **Video:** [CUDA Programming - C/C++ Basics](https://www.youtube.com/watch?v=kyL2rj_Se3M) (matrix operations sections, under 30 min)
  - **Blog Post:** [Is it possible to modify the low level convolution operation?](https://discuss.pytorch.org/t/is-it-possible-to-modify-the-low-level-convolution-operation/164775) from PyTorch Forums

### Topic 2.3: Tensor Cores Support
- **Concepts:**
  - TensorFloat-32 (TF32) on Ampere and later devices
  - Mixed precision training with FP16/BF16
  - Automatic Mixed Precision (torch.cuda.amp)
  - Performance and accuracy trade-offs

- **Recommended Resources:**
  - **Video:** [NVIDIA Tensor Core Programming](https://www.youtube.com/watch?v=Yt1A-vaWTck) (under 30 min)
  - **Blog Post:** [Programming Tensor Cores in CUDA 9](https://developer.nvidia.com/blog/programming-tensor-cores-cuda-9/) by NVIDIA

## Module 3: Advanced GPU Optimization Techniques

### Topic 3.1: CUDA Graphs
- **Concepts:**
  - Reducing CPU overhead with CUDA Graphs
  - Capturing and replaying GPU operations
  - Stream capture mode
  - Use cases and limitations

- **Recommended Resources:**
  - **Video:** [ACCELERATING PYTORCH NETWORKS WITH NATIVE CUDA GRAPHS](https://www.youtube.com/watch?v=ebEulxb1tNw) (under 30 min)
  - **Blog Post:** [Accelerating PyTorch with CUDA Graphs](https://pytorch.org/blog/accelerating-pytorch-with-cuda-graphs/) by PyTorch

### Topic 3.2: JIT Compilation
- **Concepts:**
  - Just-in-Time compilation for CUDA kernels
  - TorchScript with CUDA support
  - Fusion of operations for better performance
  - Runtime optimization of GPU code

- **Recommended Resources:**
  - **Video:** [MoreVMs'22 - Torchy: A Tracing JIT Compiler for PyTorch](https://www.youtube.com/watch?v=9DZqf83Xapc) (relevant sections, under 30 min)
  - **Blog Post:** [Optimizing models using the PyTorch JIT](https://lernapparat.de/jit-optimization-intro) by Lernapparat

### Topic 3.3: Profiling and Debugging Tools
- **Concepts:**
  - CUDA events for timing
  - NVTX range annotations
  - Integration with NVIDIA profiling tools
  - Memory usage tracking and optimization

- **Recommended Resources:**
  - **Video:** [Lecture 1 How to profile CUDA kernels in PyTorch](https://www.youtube.com/watch?v=LuhJEEJQgUM) by GPU MODE (under 30 min)
  - **Blog Post:** [Optimizing CUDA Memory Transfers with NVIDIA Nsight Systems](https://developer.nvidia.com/blog/optimizing-cuda-memory-transfers-with-nsight-systems/) by NVIDIA

## Module 4: Multi-GPU and Distributed Computing

### Topic 4.1: Multi-GPU Support
- **Concepts:**
  - Data Parallel (DP) and Distributed Data Parallel (DDP)
  - NCCL integration for efficient multi-GPU communication
  - Peer-to-peer memory access between GPUs
  - Model parallelism capabilities

- **Recommended Resources:**
  - **Video:** [02. torch.nn.DataParallel](https://www.youtube.com/watch?v=p_66I5BPPo8) (under 30 min)
  - **Blog Post:** [C++ Multiple GPUs for inference](https://discuss.pytorch.org/t/c-multiple-gpus-for-inference/133006) from PyTorch Forums

### Topic 4.2: Distributed Data Parallel
- **Concepts:**
  - Scaling training across multiple GPUs and nodes
  - Gradient synchronization strategies
  - Performance optimization for distributed training
  - Fault tolerance in distributed settings

- **Recommended Resources:**
  - **Video:** [Data Parallelism Using PyTorch DDP | NVAITC Webinar](https://m.youtube.com/watch?v=azLCUayJJoQ) (relevant sections, under 30 min)
  - **Blog Post:** [Libtorch C++ multiple GPU performance slower than single GPU](https://github.com/pytorch/pytorch/issues/40581) from PyTorch GitHub Issues

## Module 5: Specialized GPU Operations

### Topic 5.1: Custom CUDA Extensions
- **Concepts:**
  - C++/CUDA extension mechanisms
  - JIT compilation of CUDA kernels
  - torch.utils.cpp_extension for building custom CUDA operations
  - Integration with PyTorch's autograd system

- **Recommended Resources:**
  - **Video:** [Lightning Talk: Extending PyTorch with Custom Python/C++/CUDA Operators](https://www.youtube.com/watch?v=LI3h8aVchwo) by Richard Zou (under 30 min)
  - **Blog Post:** [Custom C++ and CUDA Extensions](https://pytorch.org/tutorials/advanced/cpp_extension.html) from PyTorch tutorials

### Topic 5.2: FFT and Signal Processing
- **Concepts:**
  - cuFFT integration for Fast Fourier Transforms
  - Signal processing operations on GPU
  - Plan caching for repeated FFT operations
  - Optimization for different input sizes

- **Recommended Resources:**
  - **Video:** [CUDA Programming - C/C++ Basics](https://www.youtube.com/watch?v=kyL2rj_Se3M) (FFT sections, under 30 min)
  - **Blog Post:** [Custom C++ and CUDA Operators](https://pytorch.org/tutorials/advanced/cpp_custom_ops.html) from PyTorch tutorials

### Topic 5.3: Sparse Operations
- **Concepts:**
  - GPU-accelerated sparse matrix operations
  - Sparse-dense matrix multiplications
  - Custom sparse formats optimized for GPU
  - Sparse convolutions and other neural network operations

- **Recommended Resources:**
  - **Video:** [Cuda Tutorial 12: Thrust Transforms](https://www.youtube.com/watch?v=0QyxQFq9zV4) (sparse operations sections, under 30 min)
  - **Blog Post:** [PyTorch C++ API](https://pytorch.org/cppdocs/) - Sparse tensor documentation section

## Module 6: Advanced GPU Features

### Topic 6.1: Random Number Generation
- **Concepts:**
  - CUDA-accelerated random number generators
  - Support for various distributions
  - Reproducible random sequences with seeds
  - Efficient parallel generation

- **Recommended Resources:**
  - **Video:** [CUDA C Basics](https://www.youtube.com/watch?v=2NgpYFdsduY) by cat blue (RNG sections, under 30 min)
  - **Blog Post:** [PyTorch Internals, cuRAND, and numerical instability](https://rachitsingh.com/pytorch/) by Rachit Singh

### Topic 6.2: Quantization Support
- **Concepts:**
  - INT8 and other reduced precision formats
  - GPU-accelerated quantized operations
  - Integration with TensorRT for inference
  - Dynamic quantization capabilities

- **Recommended Resources:**
  - **Video:** [NVIDIA Tensor Core Programming](https://www.youtube.com/watch?v=Yt1A-vaWTck) (quantization sections, under 30 min)
  - **Blog Post:** [Using Tensor Cores for Mixed-Precision Scientific Computing](https://developer.nvidia.com/blog/tensor-cores-mixed-precision-scientific-computing/) by NVIDIA

## Module 7: Contributing to PyTorch GPU Components

### Topic 7.1: PyTorch GPU Architecture
- **Concepts:**
  - Understanding PyTorch's CUDA architecture
  - Component interactions and dependencies
  - Performance bottlenecks and optimization opportunities
  - Testing GPU code in PyTorch

- **Recommended Resources:**
  - **Video:** [PyTorch C++ API: Updating blogs to the latest version](https://www.youtube.com/watch?v=DB3Wxq2pS7E) (under 30 min)
  - **Blog Post:** [PyTorch – Internal Architecture Tour](https://blog.christianperone.com/2018/03/pytorch-internal-architecture-tour/) by Christian Perone

### Topic 7.2: Contributing to PyTorch
- **Concepts:**
  - Setting up PyTorch development environment
  - Understanding PyTorch's contribution workflow
  - Testing and benchmarking GPU code
  - Code review process for GPU-related contributions

- **Recommended Resources:**
  - **Video:** [Bonus Lecture: CUDA C++ llm.cpp](https://www.youtube.com/watch?v=WiB_3Csfj_Q) (relevant sections, under 30 min)
  - **Blog Post:** [Contributing to PyTorch](https://github.com/pytorch/pytorch/blob/master/CONTRIBUTING.md) from PyTorch GitHub repository

## Assessment Methods
- Hands-on programming assignments for each module
- Performance optimization challenges with GPU-accelerated PyTorch components
- Mini-projects implementing custom CUDA extensions for PyTorch
- Final project: Contributing a performance improvement to a PyTorch GPU component

## Prerequisites
- Strong C++ programming skills
- Basic understanding of parallel computing concepts
- Experience with PyTorch for deep learning
- Completion of the GPU Programming Syllabus or equivalent knowledge
- Access to NVIDIA GPU hardware for practice

## Learning Outcomes
Upon completion of this syllabus, you will be able to:
1. Understand the architecture and implementation of PyTorch's GPU components
2. Optimize PyTorch code for maximum GPU performance using C++
3. Implement custom CUDA extensions for PyTorch
4. Debug and profile GPU code in PyTorch
5. Contribute effectively to PyTorch's GPU-related codebase
6. Implement advanced GPU programming techniques in PyTorch
7. Understand the trade-offs between different GPU optimization strategies
