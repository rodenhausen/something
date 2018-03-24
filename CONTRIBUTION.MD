# Charaparser Architecture
The Charaparser framework allows for different types of executions and is highly configurable to the a very detailed processing level. To configure Charaparser for a specific execution the dependency injection framework Guice is utilized. On a high level, it can for example be executed to markup descriptions, or to evaluate created markup. On another level it may for example be configured to markup morphological descriptions or a range of other description types such as habitat, elevation, phenology, ecology and distribution. Also the strategies used to markup any of these types can be varied, all the way to detailed information such as which measurement units are supported (cm, dm, m, etc.). 

The following diagram illustrates the utilization of the injection framework. AbstractModule is a Guice class utilized to create the injection bindings. The hierarchy inheriting AbstractModule merely layers configuration options of CharaParser by detail level of parameters and finally taxon groups. A command line interface (CLI) entry point may parse command line options and parameters to update the configuration for the run according to users inputs. Eventually a IRun will be created to run the desired configured execution.
![Run and Guice Configuration][Run]

[Run]: http://biosemantics.github.io/charaparser/Run.png "Run and Guice Configuration"



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

