# Polyscape: A 2D Java Game Engine

Welcome to Polyscape, a 2D game engine built in Java, designed to make game development accessible and enjoyable for
beginners, while still offering powerful features for more experienced developers. Leveraging libraries such as LWJGL,
Box2D, and Pkl, Polyscape aims to provide a comprehensive toolset for creating engaging 2D games.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Dependencies](#dependencies)
- [Configuration](#configuration)
- [Examples](#examples)
- [Contributing](#contributing)
- [License](#license)

## Introduction

Polyscape is currently under development, with a focus on simplifying the game development process. It features an
easy-to-use GUI, known as the Polyscape-Editor, which allows developers to design custom UIs and levels directly within
the engine. Our mission is to lower the barrier to entry for game development and provide a flexible platform for
creating 2D games.

## Features

- Beginner-Friendly: An intuitive interface and workflow, making game development accessible to newcomers.
- Polyscape-Editor: A built-in editor for creating and editing UIs and levels without needing to write code.
- Customizable UIs: Tools to design your game's interface exactly how you envision it.
- Level Creation: Easily design levels with our drag-and-drop editor.
- Sprite Sheet Friendly: Easily add your own sprite sheet's to objects with no hassle.
- Powered by LWJGL and Box2D: Leverages these powerful libraries for graphics, physics, and more.

## Installation

Polyscape uses Gradle for building the project, ensuring a smooth setup process. To get started, follow these steps:

1. Clone the repository:
     ```bash
    git clone https://github.com/yourusername/polyscape.git
     ```
2. Navigate to the Polyscape directory:
     ```bash
     cd polyscape
     ```
3. Build the project with Gradle:

     ```bash
    ./gradlew build
     ```

## Usage

After installing Polyscape, you can start using the engine by launching the Polyscape-Editor. Here's a quick start
guide:

1. Launch the Polyscape-Editor from your build directory.
2. Use the GUI to create your first project.
3. Begin designing your game's UI and levels using the editor's tools.
4. Test your game directly within the editor.

For detailed instructions, refer to the [Documentation](#documentation) section.

## Dependencies

Polyscape depends on the following libraries:

- [LWJGL](https://www.lwjgl.org/) (Lightweight Java Game Library)
- [Box2D](https://box2d.org/) for physics simulations
- [Pkl](https://pkl-lang.org/) for additional functionalities

Ensure these are correctly set up in your project. Gradle should handle most of the dependency management for you.

## Configuration

Most configuration in Polyscape can be done directly through the Polyscape-Editor. For advanced configurations, please
refer to the configuration files in the config directory. Or Download Polyscape-engine Jar and import the library into a Java Project

## Examples

Check out the [examples directory](https://github.com/Madmegsox1/PolyScape-Engine/tree/master/src/test/java/org/polyscape/test) for sample projects demonstrating how to use Polyscape for various types of games.

## Contributing

Contributions are welcome! Whether you're fixing bugs, adding new features, or improving the documentation, your help is
greatly appreciated. Please refer to our [contributing guidelines](https://github.com/Madmegsox1/PolyScape-Engine/blob/master/CONTRIBUTING.md) for more information on how to contribute.

## License

Polyscape is licensed under the Polyscape Engine License (PEL) Version 1.0. See the [LICENSE file](https://github.com/Madmegsox1/PolyScape-Engine/blob/master/LICENSE.md) for more details.

[comment]: # (This actually is the most platform independent comment)



[comment]: # (## Troubleshooting TODO)

[comment]: # (For common issues and their solutions, see the Troubleshooting section of our documentation. If you encounter any)

[comment]: # (problems not covered there, please open an issue on our GitHub repository.)

[comment]: # (## Documentation TODO)

[comment]: # (Comprehensive documentation is available here link to documentation. It includes detailed guides on getting started,)

[comment]: # (using the Polyscape-Editor, and developing your first game.)
