flowchart TD
A[User Interface\nStreamlit] --> B[Input Handler\nDrag & Drop / File Browser]
B --> C[File Processor\nExtract Email Text]
C --> D[DeepSeek R1 Engine\nClassification & Sub-classification]
D --> E[JSON Formatter]
E --> F[Output Display\nResults + Visualizations]
