# Matrix Slices

## Project Description

A Java library (JDK 21) implementing a hierarchy of classes for mathematical operations on 0D (scalars), 1D (vectors), and 2D (matrices) arrays of `double` values. The key feature of this library is the ability to create "slices" (views) of arrays that share data with the original object, allowing efficient sub-array manipulation.

## Key Features

-   **Polymorphic Hierarchy:** Interface-based hierarchy with `Array` as the root, extended by `Scalar`, `Vector`, and `Matrix` interfaces. Implementations include standard backing arrays (`ArrayVector`, `ArrayMatrix`) and view-based slices (`SliceVector`, `SliceMatrix`).
-   **Arithmetic Operations:** Supports addition (`getSum`), multiplication (`getProduct`), and negation. Handles mixed-type operations (e.g., adding a scalar to a matrix, multiplying a matrix by a vector) with automatic broadcasting and dimension checks.
-   **Slicing:** Create lightweight views (slices) of arrays without copying data. Modifications to a slice affect the original array.
-   **Assignments:** Flexible assignment logic allowing scalars or vectors to be assigned to higher-dimensional structures (broadcasting).
-   **Indexing:** Unified access to elements via indices, including support for variable arguments.
-   **Matrix Operations:** Includes transposition, dot products, and cross products (resulting in matrices).
-   **Error Handling:** Custom checked exceptions (`ShapeMismatch`, `InvalidIndexes`, `VectorOrientationMismatch`) enforcing strict error handling.

## Project Structure

-   `src/`: Java source files organized by packages.
-   `src/test/`: Unit tests implemented using simple Java assertions or JUnit (if configured), validating all arithmetic and slicing scenarios described in the assignment.

## Usage

The library allows intuitive mathematical syntax:
```java
Matrix m = new ArrayMatrix(3, 3);
Vector v = new ArrayVector(3);
try {
    Array result = m.getProduct(v); // Matrix-vector multiplication
} catch (ShapeMismatch e) {
    e.printStackTrace();
}
```

## What I Learned

-   Designing complex object-oriented hierarchies and using polymorphism effectively.
-   Implementing mathematical broadcasting rules and matrix algebra from scratch.
-   Managing shared mutable state (slices vs. original arrays) in Java.
-   Creating robust exception handling for mathematical constraints.
