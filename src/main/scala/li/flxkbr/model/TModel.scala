package li.flxkbr.model

class TModel(val name: ModelName, val referencedModels: Set[ModelName], val fields: Seq[ModelField]) {}
