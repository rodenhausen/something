# Charaparser Architecture
The Charaparser framework is highly configurable and allows for different types of executions.

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




Different types of descriptions


/*haraParser is a NLP tool which processes morphological descriptions of the biodiversity domain. The output of CharaParser is a structured description of structures, their characters and relations between structures in XML format.
*/


##

Charaparser transforms each description element independently.

