# fulibMockups

[![Java CI](https://github.com/fujaba/fulibMockups/workflows/Java%20CI/badge.svg)](https://github.com/fujaba/fulibMockups/actions)
[![javadoc](https://javadoc.io/badge2/org.fulib/fulibMockups/javadoc.svg)](https://javadoc.io/doc/org.fulib/fulibMockups)

A library for generating HTML mockups and web apps. Primarily used together
with [fulibScenarios](https://github.com/fujaba/fulibScenarios).

## Installation

`build.gradle`:

```groovy
repositories {
   // ...
   mavenCentral()
}
```

```groovy
dependencies {
   // ...
   // https://mvnrepository.com/artifact/org.fulib/fulibMockups
   fulibScenarios group: 'org.fulib', name: 'fulibMockups', version: '0.4.0'
   testImplementation group: 'org.fulib', name: 'fulibMockups', version: '0.4.0'
}
```

```groovy
tasks.withType(org.fulib.gradle.ScenariosTask) {
   extraArgs += [
      '--imports', 'org.fulib.mockups',
      '--diagram-handlers', '.html=import(org.fulib.scenarios.MockupTools).htmlTool().dumpScreen(%s, %s)',
      '--diagram-handlers', '.html.png=import(org.fulib.scenarios.MockupTools).htmlTool().dump(%s, %s)',
      '--diagram-handlers', '.tables.html=import(org.fulib.scenarios.MockupTools).htmlTool().dumpTables(%s, %s)',
      '--diagram-handlers', '.mockup.html=import(org.fulib.scenarios.MockupTools).htmlTool().dumpMockup(%s)',
      '--diagram-handlers', '.txt=import(org.fulib.scenarios.MockupTools).htmlTool().dumpToString(%s, %s)',
   ]
}
```

## License

[MIT](LICENSE.md)
