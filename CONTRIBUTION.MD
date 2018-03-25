# Charaparser Architecture
The Charaparser framework allows for different types of executions and is highly configurable to the a very detailed processing level. To configure Charaparser for a specific execution the dependency injection framework [Guice](https://github.com/google/guice) is utilized. On a high level, it can for example be executed to markup descriptions \[[0](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/run/etc/ETCMarkupRun.java )\], to evaluate created markup \[[1](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/run/DescriptionEvaluationRun.java)\], or others [[2](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/run/DescriptionMarkupAndDescriptionMarkupEvaluationRun.java ), [3](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/run/DescriptionMarkupRun.java ), [4](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/run/etc/ETCLearnRun.java ), ...]. On another level it may for example be configured to markup morphological descriptions \[[5](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/markup/DescriptionMarkupCreator.java )\] or a range of other description types such as habitat \[[6](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/habitatDescr/markup/HabitatMarkupCreator.java )\], elevation \[[7](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/elevation/markup/ElevationMarkupCreator.java)\], phenology \[[8](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/phenology/markup/PhenologyMarkupCreator.java)\], ecology \[[9](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/ecology/markup/EcologyMarkupCreator.java)\] and distribution \[[10](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/distribution/markup/DistributionMarkupCreator.java)\]. Also the strategies used to markup any of these types can be varied, all the way to detailed information such as which measurement units are supported (cm, dm, m, etc.) \[[11](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/config/BasicConfig.java#L330)\]. 

The following two diagrams illustrate the utilization of the injection framework. [AbstractModule](https://google.github.io/guice/api-docs/latest/javadoc/index.html?com/google/inject/AbstractModule.html) is a Guice class utilized to create the injection bindings. The hierarchy inheriting AbstractModule merely layers configuration options of CharaParser by detail level (i.e. BaseConfig \[[12](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/config/BasicConfig.java)\] contains parameters that likely do not vary, while RunConfig \[[13](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/config/RunConfig.java)\] contains parameters that frequently change between runs) and finally taxon groups \[[14](https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/config/taxongroup)\]. A command line interface (CLI) entry point \[[15](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/CLIMain.java)\] may parse \[[16](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/CLIMain.java#L96)\] command line options and parameters to update the configuration for the run according to users inputs. Eventually a IRun will be created \[[17](https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/CLIMain.java#L72)\] to execute the desired run configuration.

![Run and Guice Configuration][Run]
![ETCLearnMain][ETCLearnMain]

ETCLearnRun is one IRun example that is used for illustration in the following. For the markup of morphological descriptions it is required to learn the terminology used in the description. This is achieved by ETCLearnRun. To achieve this it will learn using OTOLearner. OTOLearner reads the descriptions, e.g. from XML files and returns the model class DescriptionsFilesList. It will then retrieve a glossary for the taxon group configured from OTO and subsequently initialize an in memory glossary with the retrieved data. An instance of ITerminologyLearner will proceed with the actual learning of terminology from the DescriptionsFileList. Finally OTOLearner will send the the collection of terminology found for review and corrective relabeling to OTO2, the term categorization application.

![ETCLearnRun][ETCLearnRun]

ETCLearnRun is usually only run in conjunction with ETCMarkupRun, another IRun option. ETCMarkupRun utilizes a MarkupChain that trigger the creation of markup for a configured set of description types. 

![ETCMarkupRun][ETCMarkupRun]

The currently available IMarkupCreator, on a high level, all very much behave the same way. They read the type of description which they are supposed to transform. They will do the transformation and they will write the transformed description back to the source, e.g. an XML file. To illustrate this take a look at the DescriptionMarkupCreator (Markup Creator for descriptions of type morphologye). DescriptionMarkupCreator will loop over a set of configured IDescriptionTransformer to create the transformed morphological description. For example, a first transformer may extract create biological entities and their characters from a piece of text, while a subsequent transformer may map biological entities found to IRIs in ontologies available.

![DescriptionMarkupCreator][DescriptionMarkupCreator]

The following illustrates the architecture specific to morphological descriptions. It is considerably simplified for any of the other description types. 

To dive deeper into this, let's take a look at the IDescriptionTransformer that is MarkupDescriptionTreatmentTransformer. This transformer will retrieve the glossary for the specific taxon group from OTO and additionally retrieve the reviewed term categorizations from the learn step discussed previously. It initializes an in-memory glossary from these two data sources and learn using an instance of DatabaseInputNoLearner. As the name suggested this is not actually learning terminology but rather initializing a required database. Finally for all descriptions to be transformed it will extract the description markup using a DescriptionExtractorRun.

![MarkupDescriptionTreatmentTransformer][MarkupDescriptionTreatmentTransformer]

DescriptionExtractorRun loops over all sentences and chunks each one of them resulting in a ChunkCollector. Once complete, it extracts the marked up description using all the ChunkCollectors.

![DescriptionExtractorRun][DescriptionExtractorRun]

To do the chunking of a sentence, in detail, SentenceChunkerRun normalizes, tokenizes, tags, and parses the sentence. In the last step a ChunkerChain chunks the sentence resulting in a ChunkCollector.

![SentenceChunkerRun][SentenceChunkerRun]

To do extract the mraked up description using all the ChunkCollectors of a single description, in detail, the SomeDescriptionExtractor first creates a ProcessingContext. It will iterate all ChunkCollectors, each of them corresponding to one of the sentences in the description. It will initialize the ProcessingContext for the new sentence by resetting and setting the ChunkCollector. It will get and loop over all the chunks of the sentence, obtain the IChunkProcessor adequate for the ChunkType at hand and process it resulting in a set of Elements. This concludes the markup process of morphological descriptions on a high level.

![SomeDescriptionExtractor][SomeDescriptionExtractor]




[Run]: http://biosemantics.github.io/charaparser/Run.png "Run and Guice Configuration"
[ETCLearnMain]: http://biosemantics.github.io/charaparser/ETCLearnMain.png "ETCLearnMain"
[ETCLearnRun]: http://biosemantics.github.io/charaparser/ETCLearnRun.png "ETCLearnRun"
[ETCMarkupRun]: http://biosemantics.github.io/charaparser/ETCMarkupRun.png "ETCMarkupRun"
[DescriptionMarkupCreator]: http://biosemantics.github.io/charaparser/DescriptionMarkupCreator.png "DescriptionMarkupCreator"
[MarkupDescriptionTreatmentTransformer]: http://biosemantics.github.io/charaparser/MarkupDescriptionTreatmentTransformer.png "MarkupDescriptionTreatmentTransformer"
[DescriptionExtractorRun]: http://biosemantics.github.io/charaparser/DescriptionExtractorRun.png "DescriptionExtractorRun"
[SomeDescriptionExtractor]: http://biosemantics.github.io/charaparser/SomeDescriptionExtractor.png "SomeDescriptionExtractor"
[SentenceChunkerRun]: http://biosemantics.github.io/charaparser/SentenceChunkerRun.png "SentenceChunkerRun"









The different types of executions manifest in different entry points, e.g. 

Different types of executions and thus different types of entry points.
parse/eval/..
learn and parse portion

https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/run/etc/ETCLearnRun.java
https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/run/etc/ETCMarkupRun.java
https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/run/iplant/IPlantLearnRun.java
https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/run/iplant/IPlantMarkupRun.java
https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/run/DescriptionMarkupRun.java
https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/run/DescriptionMarkupAndDescriptionMarkupEvaluationRun.java
https://github.com/biosemantics/charaparser/blob/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/run/DescriptionEvaluationRun.java

Configuration
For the configuration of charaparser runs Google Guice is used as dependency framework. A hierarchy of configurations can be utilized to provide a runs' configuration. There exists a predefined set of configuration based on taxon group.
https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/config
https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/config/taxongroup

Entry points
https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup

ETCMarkupRun delegates a IMarkupCreator (e.g. a MarkupChain) to create the markup. The MarkupChain will create the markup for the different description types with the respective I{DescriptionType}MarkupCreators as provided.

These can be found based on {DescriptionType} in the packages here: https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement

e.g. for type morphology (still called description from a time when there was only one type of description: morphology)
https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description
for type phenology:
https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/phenology

Looking closer at phenology:
There are components that will 
- io: provide Input/Output capabilities of phenology information from the data files
- model: the data model of phenology data retrieved from files and to be written to files after the markup process is complete
- markup: defines what the markup process of phenology is like
- transform: transforms the phenology data from input to output schema

Even though the technique used to parse and transform the type of data may differ, at this level this is opague and thus this basic process is very much the same with the other types of descriptions. 

Morphological descriptions also follow the same pattern of processing. However, given the complexity of morphological text the transformation process is more sophisticated. The transformation process of description relies on a learn and parse portion. Learn will use unsupervised learning (https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/ling/learn)
to learn the terminology used in the descriptions. The parse/markup portion will utilize a pipeline of NLP tools to normalize (https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/ling/normalize), tokenize, pos tag (https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/ling/pos/lib), sentence syntax first parse (https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/ling/parse) the descriptions. Then a set of chunkers (https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/ling/chunk) will, based on the results of the NLP tool pipeline chunk sentence tokens logically belonging together. Description extracts (https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/ling/extract, https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/markupelement/description/ling/extract) will then use these chunks to build the data according to the output schema, i.e. create Characters and Relations. These processes may rely on knowledge resources (https://github.com/biosemantics/charaparser/tree/master/src/main/java/edu/arizona/biosemantics/semanticmarkup/ling/know/lib).



Different types of descriptions


/*haraParser is a NLP tool which processes morphological descriptions of the biodiversity domain. The output of CharaParser is a structured description of structures, their characters and relations between structures in XML format.
*/


##

Charaparser transforms each description element independently.

