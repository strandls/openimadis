package plugins.strand.strandimanagelogin;

/**
 * Number of microscopes in the fascility
 * 
 * @author Anup Kulkarni
 */
public enum FascilitySize {
	Small
	{
		@Override
		public String toString()
		{
			return "< 5 Microscopes";
		}
	},
	Moderate
	{
		@Override
		public String toString()
		{
			return "5 to 15 Microscopes";
		}
	},
	Large
	{
		@Override
		public String toString()
		{
			return "> 15 Microscopes";
		}
	}
}
